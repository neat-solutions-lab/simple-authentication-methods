package nsl.sam.functional.basicauth

import nsl.sam.FunctionalTestConstants
//import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD
import nsl.sam.FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import nsl.sam.FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT
import nsl.sam.FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME
import nsl.sam.FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.BasicAuthConfiguration
import nsl.sam.spring.config.DisableBasicAuthConfig
import nsl.sam.spring.config.TokenAuthConfiguration
import org.springframework.mock.web.MockHttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
//import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class NarrowConfBasicAuthFT {

//    @get:Rule
//    val thrown: ExpectedException = ExpectedException.none()

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Autowired
    private lateinit var filterChain: FilterChainProxy

    //
    // Main beans arrangement
    //

    @Test
    fun basicAuthConfigBeanPresent() {
        this.ctx.getBean(BasicAuthConfiguration::class.java)
    }

    @Test
    fun tokenAuthConfigBeanNotPresent() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            this.ctx.getBean(TokenAuthConfiguration::class.java)
        }
    }

    @Test
    fun disableBasicAuthConfigBeanNotPresent() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            this.ctx.getBean(DisableBasicAuthConfig::class.java)
        }
    }

    //
    // Main filters arrangement
    //

    @Test
    fun basicAuthenticationFilterInFilterChainWhenDefaultMethodsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNotNull(filter)
    }

    @Test
    fun noTokenAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNull(filter)
    }

    //
    // Requests against MockMVC
    //

    @Test
    fun userAuthenticatedAsValidUserFromPasswordsFile() {
        mvc.perform(
                MockMvcRequestBuilders
                        .get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                        .with(
                                httpBasic(
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                        )
        ).andExpect(jsonPath("$.username", Matchers.equalTo("test")))
    }

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
    fun unauthorizedHttpBasicWhenNotAuthenticationHeader() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedTokenWhenOnlyHttpBasicIsEnabled() {
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

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_BASIC_AUTH])
    class TestConfiguration : FakeControllerConfiguration()

}
