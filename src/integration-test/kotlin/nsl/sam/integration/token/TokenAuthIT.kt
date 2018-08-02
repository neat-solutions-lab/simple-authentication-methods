package nsl.sam.integration.token

import nsl.sam.IntegrationTestConstants
import nsl.sam.logger.logger
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.*
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TokenAuthITConfig::class])
@TestPropertySource(properties = [
    "sam.passwords-file=src/integration-test/config/passwords.conf",
    "sam.tokens-file=src/integration-test/config/tokens.conf"])
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

    // HTTP unauthorized status, for simple-authentication-methods library, really means unauthenticated
    @Test
    fun forbiddenHttpBasicWhenOnlyTokenMethodIsEnabled() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
        ).getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        log.info("################################################################################")
        log.info("response: ${response.body}")
        log.info("################################################################################")

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }
}