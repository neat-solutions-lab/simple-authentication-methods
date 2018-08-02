package nsl.sam.functional.token

import nsl.sam.FunctionalTestConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import nsl.sam.FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT
import nsl.sam.logger.logger
import org.springframework.mock.web.MockHttpServletResponse

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [TokenAuthFunctionalTestConfig::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class TokenAuthFunctionalTest {

    companion object { val log by logger() }

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun successAuthenticationWithTokenAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
            .perform(
                get(MOCK_MVC_TEST_ENDPOINT).header(
                    FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                )
            )
            .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        assertThat(response.contentAsString).isEqualTo(FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun forbiddenAuthenticationWithBasicAuthWhenOnlyTokenIsEnabled() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                    get(MOCK_MVC_TEST_ENDPOINT)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                        )
                )
                .andReturn().response

        log.info("###########################################################################")
        log.info("content: ${response.contentAsString}")
        log.info("###########################################################################")

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun failedAuthenticationWithTokenWhenWrongToken() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                    get(MOCK_MVC_TEST_ENDPOINT).header(
                        FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE
                    )
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithTokenWhenNoAuthenticationHeader() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        log.info("###########################################################################")
        log.info("content: ${response.contentAsString}")
        log.info("###########################################################################")

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }
}
