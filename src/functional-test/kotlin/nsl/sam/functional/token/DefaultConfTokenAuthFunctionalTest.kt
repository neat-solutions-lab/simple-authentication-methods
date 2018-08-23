package nsl.sam.functional.token

import nsl.sam.FunctionalTestConstants
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import nsl.sam.spring.config.TokenAuthConfig
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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.hamcrest.Matchers.equalTo
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [DefaultConfTokenAuthFunctionalTestConfig::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class DefaultConfTokenAuthFunctionalTest {

    companion object { val log by logger() }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var filterChain: FilterChainProxy

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Autowired
    private lateinit var securityConfigurer: WebSecurityConfigurerAdapter

    @Autowired
    private lateinit var localFileTokensToUserMapper: TokenToUserMapper

    //
    // Main beans arrangement
    //

    @Test
    fun basicAuthConfigBeanPresent() {
        this.ctx.getBean(BasicAuthConfig::class.java)
    }

    @Test
    fun tokenAuthConfigBeanPresent() {
        this.ctx.getBean(TokenAuthConfig::class.java)
    }

    @Test
    fun disableBasicAuthConfigBeanNotPresentWhenBasicAuthIsNotDisabled() {
        thrown.expect(NoSuchBeanDefinitionException::class.java)
        this.ctx.getBean(DisableBasicAuthConfig::class.java)
    }

    //
    // Main filters arrangement
    //

    @Test
    fun tokenAuthenticationFilterInFilterChainWhenSimpleTokenIsEnabled() {
        val filter = filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNotNull(filter)
    }

    @Test
    fun basicAuthenticationFilterInFilterChainWhenNotDisabled() {
        val filter = filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNotNull(filter)
    }

    //
    // Users mappings
    //

    @Test
    fun localFileTokensToUserMapperActiveWhenSimpleTokenMethodIsEnabled() {
        val userAndRoles = localFileTokensToUserMapper.mapToUser("12345")
        assertEquals("tester", userAndRoles.name)
    }

    @Test
    fun localUserDetailsServiceActiveWhenBasicAuthNotDisabled() {
        val userDetailsService = securityConfigurer.userDetailsServiceBean()
        val userDetails = userDetailsService.loadUserByUsername("test")
        assertEquals("test", userDetails.username)
    }

    //
    // Requests against MockMVC
    //

    @Test
    fun userAuthenticatedAsValidUserFromTokensFile() {
        mvc.perform(
                get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                        .header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                )
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("tester")))
    }

    @Test
    fun successAuthenticationWithTokenAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithTokenWhenWrongToken() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedAuthenticationWithTokenWhenNoToken() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
