package nsl.sam.functional.token

import nsl.sam.FunctionalTestConstants
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import nsl.sam.FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.tokendetails.TokenDetailsService
import nsl.sam.utils.JsonUtils
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.tokens-file=src/functional-test/config/tokens.conf"
])
class NarrowConfTokenAuthFT {

    companion object {
        val log by logger()
    }

    @Autowired
    lateinit var ctx: ApplicationContext

    @Autowired
    lateinit var filterChain: FilterChainProxy

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    private lateinit var securityConfigurer: WebSecurityConfigurerAdapter

    //@Autowired
    //private lateinit var localFileTokensDetailsService: TokenDetailsService

    //
    // Main beans arrangement
    //

    @Test
    fun disableBasicAuthSimpleConfigurerBeanPresent() {
        //ctx.getBean(DisableBasicAuthSimpleConfigurer::class.java)
    }

    //
    // Main filters arrangement
    //

    @Test
    fun noBasicAuthenticationFilterInFilterChainWhenOnlySimpleTokenIsEnabled() {
        val filter = filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNull(filter)
    }

    @Test
    fun tokenAuthenticationFilterInFilterChainWhenSimpleTokenIsEnabled() {
        val filter = filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNotNull(filter)
    }

    //
    // Users mappings
    //
    //@Test
    //fun localFileTokensToUserMapperActiveWhenSimpleTokenMethodIsEnabled() {
    //    val userDetails = localFileTokensDetailsService.loadUserByToken("12345")
    //    assertEquals("tester", userDetails.username)
    //}

    //
    // Request against MockMVC
    //

    @Test
    fun userAuthenticatedAsValidUserFromTokensFile() {
        mvc.perform(
                get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                        .header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo("tester")))
    }

    @Test
    fun successAuthenticationWithTokenAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        assertThat(response.contentAsString).isEqualTo(FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithTokenWhenWrongToken() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        get(MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())

        println(JsonUtils.toPretty(response.contentAsString))
    }

    @Test
    fun failedTokenWhenNoTokenProvided() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(get(MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun failedHttpBasicWhenOnlyTokenIsEnabled() {
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

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_TOKEN])
    class TestConfiguration : FakeControllerConfiguration()
}
