package nsl.sam.integration.forcehttps

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.integration.controller.IntegrationTestController
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.ResourceAccessException

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [ForceHttpsWhenServerIsHttpITConfiguration::class]
)
@EnableAutoConfiguration
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/integration-test/config/passwords.conf",
    "nsl.sam.tokens-file=src/integration-test/config/tokens.conf"
])
class ForceHttpsWhenServerIsHttpIT {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    /*
     * server is set to use HTTP but attribute passed to @EnableSimpleAuthenticationMethods
     * forces HTTPS usage, so that client receives series of redirect responses and when
     * finally threshold or redirect responses is reached, the ResourceAccessException
     * exception is thrown
     */
    @Test
    fun successAuthenticationWithBasicAuth() {

        val exception = org.junit.jupiter.api.Assertions.assertThrows(ResourceAccessException::class.java) {

            val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                    IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                    IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
            ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

            println("response: ${response.body}")
        }

        println("exception: $exception")
        println("exception message: ${exception.message}")
        println("exception cause: ${exception.cause}")
    }
}

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH], forceHttps = true)
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class ForceHttpsWhenServerIsHttpITConfiguration