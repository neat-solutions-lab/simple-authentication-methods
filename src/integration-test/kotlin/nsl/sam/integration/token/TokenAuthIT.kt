package nsl.sam.integration.token

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.integration.controller.IntegrationTestController
import nsl.sam.logger.logger
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.*
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestConfiguration::class])
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/integration-test/config/passwords.conf",
    "nsl.sam.tokens-file=src/integration-test/config/tokens.conf"])
class TokenAuthIT {

    companion object {
        val log by logger()
    }

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun successAuthenticationWithCorrectToken() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME, IntegrationTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE)
        val requestHttpEntity = HttpEntity<String>("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT, HttpMethod.GET, requestHttpEntity, String::class.java)

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(exchange.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    // it should be unauthorized
    @Test
    fun unauthorizedWithIncorrectToken() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME, IntegrationTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE)
        val requestHttpEntity = HttpEntity<String>("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT, HttpMethod.GET, requestHttpEntity, String::class.java)

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedTokenWhenNoTokenProvided() {
        // ACT
        val response: ResponseEntity<String> =
                testRestTemplate.getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedHttpBasicWhenOnlyTokenIsEnabled() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
        ).getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}

@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_TOKEN])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class TestConfiguration
