package nsl.sam.integration.forcehttps

//import org.springframework.boot.context.embedded.
import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.annotation.attrtypes.PortsMapping
import nsl.sam.integration.controller.IntegrationTestController
import org.assertj.core.api.Assertions
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.SocketUtils
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = [ForceHttpsWhenServerUsesBothConfiguration::class]
)
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/integration-test/config/passwords.conf",
    "nsl.sam.tokens-file=src/integration-test/config/tokens.conf",
    "server.ssl.key-store=src/integration-test/config/keystore.jks",
    "server.ssl.key-store-password=123456",
    "server.ssl.keyAlias=localhost"
])
class ForceHttpsWhenServerUsesBoth {


    companion object {

        var httpPort  = 0
        var httpsPort = 0

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            httpPort = SocketUtils.findAvailableTcpPort()
            httpsPort = SocketUtils.findAvailableTcpPort()
            println("http port: $httpPort")
            println("https port: $httpsPort")
            //System.setProperty("spring.application.json",
            //        "{\"server.port\":$httpsPort, \"http.port\":$httpPort}"
            //)
            System.setProperty("server.port", "$httpsPort")
            System.setProperty("http.port", "$httpPort")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            System.clearProperty("server.port")
            System.clearProperty("http.port")
        }
    }

    private fun encodeCredentials(username: String, password: String): String {
        val concatenatedCredentials = "$username:$password"
        val encodedByteArray = Base64.getEncoder().encode(concatenatedCredentials.toByteArray())
        return String(encodedByteArray)
    }


    @Test
    fun test() {
        println("bla")
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
                .uri(URI("http://localhost:$httpPort${IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT}"))
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
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .sslContext(sslContext)
                .build()


        val response: HttpResponse<String> =
                client.send(request, HttpResponse.BodyHandlers.ofString())

        println("RESPONSE HEADERS: ${response.headers().map()["location"]}")
        println("RESPONSE BODY: ${response.body()}")

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.body()).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)

        println("response: ${response.body()}")
    }

}

@Configuration
@EnableSimpleAuthenticationMethods(
        methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH],
        forceHttps = true,
        portMapping = [PortsMappingImpl::class])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class ForceHttpsWhenServerUsesBothConfiguration {

    @Bean
    fun customContainer(): WebServerFactoryCustomizer<JettyServletWebServerFactory> {
        return CustomContainer()
    }

}

class CustomContainer:WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    override fun customize(factory: JettyServletWebServerFactory) {
        factory.addServerCustomizers(JettyServerCustomizer {

            // HTTP
            val connector = ServerConnector(it)
            connector.setPort(ForceHttpsWhenServerUsesBoth.httpPort)


            // HTTPS
            val sslContextFactory = SslContextFactory()
            sslContextFactory.setKeyStorePassword("123456")
            sslContextFactory.setKeyStorePath("src/integration-test/config/keystore.jks")
            sslContextFactory.certAlias = "localhost"

            val https = HttpConfiguration()
            https.addCustomizer(SecureRequestCustomizer())

            val sslConnector = ServerConnector(
                    it, SslConnectionFactory(sslContextFactory, "http/1.1"),
                    HttpConnectionFactory(https))
            sslConnector.setPort(ForceHttpsWhenServerUsesBoth.httpsPort)

            it.connectors = arrayOf(connector, sslConnector)
        })
    }
}

class PortsMappingImpl : PortsMapping {
    override fun getMapping(): Pair<Int, Int> {
        return ForceHttpsWhenServerUsesBoth.httpPort to ForceHttpsWhenServerUsesBoth.httpsPort
    }
}
