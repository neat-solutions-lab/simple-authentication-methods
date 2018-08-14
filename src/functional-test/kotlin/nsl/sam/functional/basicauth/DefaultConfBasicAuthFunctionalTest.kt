package nsl.sam.functional.basicauth

import nsl.sam.FunctionalTestConstants
import nsl.sam.spring.config.BasicAuthConfig
//import nsl.sam.spring.config.DisableBasicAuthConfigurer
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [DefaultConfBasicAuthFunctionalTestConfig::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class DefaultConfBasicAuthFunctionalTest {

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var ctx: ApplicationContext

//    @Test
//    fun disableBasicAuthConfigurerBeanNotPresent() {
//        this.thrown.expect(NoSuchBeanDefinitionException::class.java)
//        this.ctx.getBean(DisableBasicAuthConfigurer::class.java)
//    }

    @Test
    fun basicAuthConfigBeanPresent() {
        this.ctx.getBean(BasicAuthConfig::class.java)
    }

    @Test
    fun successAuthenticationWithBasicAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenWrongPassword() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotExistingUser() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun unauthorizedHttpBasicWhenNotAuthenticationHeader() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

}