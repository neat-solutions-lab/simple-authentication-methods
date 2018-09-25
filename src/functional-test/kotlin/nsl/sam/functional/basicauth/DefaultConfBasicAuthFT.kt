package nsl.sam.functional.basicauth

import nsl.sam.FunctionalTestConstants
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.assertj.core.api.Assertions as Assertj
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
//import org.junit.rules.ExpectedException
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class DefaultConfBasicAuthFT {

    companion object {
        val log by logger()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Autowired
    private lateinit var filterChain: FilterChainProxy

    @Autowired
    private lateinit var securityConfigurer: WebSecurityConfigurerAdapter

    @Autowired
    private lateinit var localFileTokensToUserMapper: TokenToUserMapper

    //
    // Main beans arrangement
    //

    //
    // Main filters arrangement
    //
    @Test
    fun tokenAuthenticationFilterInFilterChainWhenSimpleTokenMethodNotDisabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNotNull(filter)
    }

    @Test
    fun basicAuthenticationFilterInFilterChainWhenDefaultMethodsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNotNull(filter)
    }

    //
    // Users mappings
    //

    @Test
    fun localFileTokensToUserMapperActiveWhenSimpleTokenMethodNotDisabled() {
        val userAndRoles = localFileTokensToUserMapper.mapToUser("12345")
        assertEquals("tester", userAndRoles.name)
    }

    @Test
    fun localUserDetailsServiceActiveWhenBasicAuthIsEnabled() {
        val userDetailsService = securityConfigurer.userDetailsServiceBean()
        val userDetails = userDetailsService.loadUserByUsername("test")
        assertEquals("test", userDetails.username)
    }


    //
    // Requests against MockMVC
    //

    @Test
    fun userAuthenticatedAsValidUserFromPasswordsFile() {
        mvc.perform(
                get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                        .with(
                                httpBasic(
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                        )
        ).andExpect(jsonPath("$.username", equalTo("test")))
    }

    @Test
    fun successAuthenticationWithBasicAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        httpBasic(
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                                )
                )
                .andReturn().response

        // ASSERT
        Assertj.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertj.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenWrongPassword() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(httpBasic(FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertj.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotExistingUser() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(httpBasic(FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertj.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun unauthorizedHttpBasicWhenNotAuthenticationHeader() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        Assertj.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    class TestConfiguration : FakeControllerConfiguration()

}