package nsl.sam.functional.anonymousfallback.all

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "nsl.sam.anonymous-fallback=true",
    "server.address=localhost"
])
class NoCredentialsAndFallbackFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun successfulBasicAuthWithOtherwiseValidUserWhenNoPasswordsFile() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun successfulAuthenticationWithValidTokenOtherwiseWhenNoTokensFile() {
        val response: MockHttpServletResponse = mvc.perform(
                MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                        .header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
        ).andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("anonymousUser")
    }

    @Test
    fun successfulAccessWithNoCredentialsAtAll() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
    }



    /*
     * it should succeed only because anonymous access has been enabled
     */
    @Test
    fun userInfoEndpointReportsAnonymousUserWhenOtherBasicAuthCredentialAreProvided() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("anonymousUser")
    }

    @Test
    fun userInfoEndpointReportsAnonymousUserWhenOtherwiseValidTokenProvided() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                                .header(
                                        FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                        FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("anonymousUser")
    }

    @Test
    fun userInfoEndpointReportsAnonymousUserWhenNoCredentialsAtAll() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT))
                .andReturn().response

        println("response: ${response.contentAsString}")

        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("anonymousUser")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([
        AuthenticationMethod.SIMPLE_TOKEN,
        AuthenticationMethod.SIMPLE_BASIC_AUTH
    ])
    class TestConfiguration : FakeControllerConfiguration()
}