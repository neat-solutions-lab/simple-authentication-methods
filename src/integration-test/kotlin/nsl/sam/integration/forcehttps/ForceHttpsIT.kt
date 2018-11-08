package nsl.sam.integration.forcehttps

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.integration.controller.IntegrationTestController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.Authenticator
import java.net.PasswordAuthentication
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [ForceHttpsITConfiguration::class]
)
@EnableAutoConfiguration
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/integration-test/config/passwords.conf",
    "nsl.sam.tokens-file=src/integration-test/config/tokens.conf",
    "server.ssl.key-store=src/integration-test/config/keystore.jks",
    "server.ssl.key-store-password=123456",
    "server.ssl.keyAlias=localhost"
])
//@TestPropertySource(properties = [
//    "nsl.sam.passwords-file=src/integration-test/config/passwords.conf",
//    "nsl.sam.tokens-file=src/integration-test/config/tokens.conf"
//])
class ForceHttpsIT {


    @LocalServerPort
    private var portNumber: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @BeforeEach
    fun init() {

    }

    private fun encodeCredentials(username: String, password: String): String {
        val concatenatedCredentials = "$username:$password"
        val encodedByteArray = Base64.getEncoder().encode(concatenatedCredentials.toByteArray())
        return String(encodedByteArray)
    }

    @Test
    fun test() {
        val uri = HttpRequest.newBuilder().uri(URI("https://localhost:$portNumber/")).build()
        println("Port number is $portNumber")
        println("URI: $uri")
    }

    @Test
    fun successAuthenticationWithBasicAuth() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        println("response: ${response.body}")

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun successAuthenticationWithBasicAuthAndJava11Client() {

        val encodedCredentials = encodeCredentials(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
        )

        println("encodedCredentials $encodedCredentials")

        val request = HttpRequest
                .newBuilder()
                .uri(URI("https://localhost:$portNumber${IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT}"))
                .header("Authorization", "Basic $encodedCredentials")
                .build()


        val sslContext = SSLContext.getInstance("TLS")
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        }
        sslContext.init(null, arrayOf(trustManager), null)



        val client = HttpClient
                .newBuilder()
                .sslContext(sslContext)
                .build()

        val response: HttpResponse<String> =
                client.send(request, HttpResponse.BodyHandlers.ofString())

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.body()).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)

        println("response: ${response.body()}")

    }


}

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class ForceHttpsITConfiguration