package nsl.sam.functional.basicauth

import nsl.sam.FunctionalTestConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import nsl.sam.FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT
import nsl.sam.FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME
import nsl.sam.FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD
import org.springframework.mock.web.MockHttpServletResponse

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [BasicAuthFunctionalTestConfig::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class BasicAuthFunctionalTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun successAuthenticationWithBasicAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
            .perform(
                get(MOCK_MVC_TEST_ENDPOINT)
                    .with(httpBasic(EXISTING_BASIC_AUTH_USER_NAME, EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD))
            )
            .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        assertThat(response.contentAsString).isEqualTo(FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenWrongPassword() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(MOCK_MVC_TEST_ENDPOINT)
                            .with(httpBasic(EXISTING_BASIC_AUTH_USER_NAME, EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotExistingUser() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(MOCK_MVC_TEST_ENDPOINT)
                            .with(httpBasic(NOT_EXISTING_BASIC_AUTH_USER_NAME, NOT_EXISTING_BASIC_AUTH_USER_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotAuthenticationHeader() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithTokenWhenOnlyBasicAuthIsEnabled() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                    get(MOCK_MVC_TEST_ENDPOINT).header(
                        FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                    )
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
