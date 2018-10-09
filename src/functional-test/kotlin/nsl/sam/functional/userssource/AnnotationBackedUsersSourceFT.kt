package nsl.sam.functional.userssource

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [AnnotationBasedUsersSourceFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationBackedUsersSourceFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun successAuthenticationWithFirstHardcodedUser() {
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                "hardcoded-user1",
                                                "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }


    @Test
    fun successAuthenticationWithSecondHardcodedUser() {
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                "hardcoded-user2",
                                                "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }


    @Test
    fun failedAuthenticationWithWrongPasswordForSecondHardcodedUser() {
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                "hardcoded-user2",
                                                "wrong")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithBasicAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                "test",
                                                "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(users =
[
    "hardcoded-user1:{noop}test USER ADMIN",
    "hardcoded-user2:{noop}test USER ADMIN"
])
class AnnotationBasedUsersSourceFTConfiguration : FakeControllerConfiguration()