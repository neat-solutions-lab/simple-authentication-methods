package nsl.sam.integration.annotationtoken

import nsl.sam.IntegrationTestConstants
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.integration.controller.IntegrationTestController
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [AnnotationTokensITConfiguration::class])
class AnnotationTokensIT {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun successAuthenticationWhenFirstProperTokenFromAnnotationUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer AnnotationToken001"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(exchange.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun successAuthenticationWhenSecondProperTokenFromAnnotationUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer AnnotationToken003"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(exchange.body).isEqualTo(IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWhenCommentedTokenFromAnnotationUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer AnnotationToken002"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenWrongTokenUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer WrongToken"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenEmptyTokenUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer "
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenEmptyHeaderUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                ""
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenUnknownHeaderValueUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Hello from SMS. "
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenWrongTokenFormatUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer AnnotationToken001 more stuff"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenMisspelledHeaderValueUsed() {
        // ARRANGE
        val headers = HttpHeaders()
        headers.set(
                IntegrationTestConstants.TOKEN_AUTH_HEADER_NAME,
                "Bearer: AnnotationToken001"
        )
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun failedAuthenticationWhenNoHeader() {
        // ARRANGE
        val headers = HttpHeaders()
        val requestHttpEntity = HttpEntity("", headers)

        // ACT
        val exchange: ResponseEntity<String> = testRestTemplate
                .exchange(
                        IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT,
                        HttpMethod.GET,
                        requestHttpEntity,
                        String::class.java
                )

        // ASSERT
        Assertions.assertThat(exchange.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}


@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokens = [
    "AnnotationToken001 annotation-tester001 USER ADMIN ROOT",
    "#AnnotationToken002 annotation-tester002 USER ADMIN ROOT",
    "AnnotationToken003 annotation-tester003 USER ADMIN ROOT"
])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class AnnotationTokensITConfiguration