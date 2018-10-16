package nsl.sam.integration.annotationbasicauth

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.integration.controller.IntegrationTestController
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [AnnotationPasswordsITConfiguration::class])
class AnnotationPasswordsIT {

    companion object {
        val log by logger()
    }

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun successfulAuthenticationWhenFirstProperUserFromAnnotationUsed() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "annotation-tester001",
                "pass001"
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun successfulAuthenticationWhenSecondProperUserFromAnnotationUsed() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "annotation-tester003",
                "pass003"
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWhenCommentedUserFromAnnotationUsed() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "annotation-tester002",
                "pass002"
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenWrongPasswordUsed() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "annotation-tester001",
                "pass002"
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenNonExistingUserUsed() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "ghost",
                ""
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenEmptyCredentials() {
        // ACT
        val response: ResponseEntity<String> = testRestTemplate.withBasicAuth(
                "",
                ""
        ).getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenNoCredentials() {
        // ACT
        val response: ResponseEntity<String> =
                testRestTemplate.getForEntity(IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT)

        // ASSERT
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(users = [
    "annotation-tester001:{noop}pass001 USER ADMIN",
    "#annotation-tester002:{noop}pass002 USER ADMIN",
    "annotation-tester003:{noop}pass003 USER ADMIN"
])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class AnnotationPasswordsITConfiguration