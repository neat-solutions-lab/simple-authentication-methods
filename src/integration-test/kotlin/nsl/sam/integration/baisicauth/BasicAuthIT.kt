package nsl.sam.integration.basicauth

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.logger.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [TestConfiguration::class])
@TestPropertySource(properties = [
    "sam.passwords-file=src/integration-test/config/passwords.conf",
    "sam.tokens-file=src/integration-test/config/tokens.conf"])
class BasicAuthIT {

    companion object {
        val log by logger()
    }

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun successAuthenticationWithBasicAuth() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
        ).getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenWrongPassword() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD
        ).getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotExistingUser() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                IntegrationTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME,
                IntegrationTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD
        ).getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedHttpBasicWhenNotAuthenticationHeader() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate
                .getForEntity<String>(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedTokenWhenOnlyHttpBasicIsEnabled() {

        // ARRANGE
        val headers = HttpHeaders()
        headers.set(IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME, IntegrationTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE)

        val requestHttpEntity = HttpEntity<String>(headers)

        // ACT
        val response: ResponseEntity<String> = testRestTemplate
                .getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT, requestHttpEntity)

        // ASSERT
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

}

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class TestConfiguration
