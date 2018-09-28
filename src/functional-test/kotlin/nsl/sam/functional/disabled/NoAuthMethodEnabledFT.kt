package nsl.sam.functional.disabled

import nsl.sam.FunctionalTestConstants
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties =
    ["nsl.sam.anonymous-fallback=true", "server.address=localhost"])
class NoAuthMethodEnabledFT {

    companion object {
        val log by logger()
    }

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Autowired
    lateinit var webSecurityConfigurer: WebSecurityConfigurerAdapter

    @Autowired
    lateinit var filterChain: FilterChainProxy

    @Autowired
    lateinit var mvc: MockMvc

    //
    // Main beans arrangement
    //

    @Test
    fun webSecurityConfigurerBeanPresent() {
        this.ctx.getBean(WebSecurityConfigurerAdapter::class.java)
    }

    //
    // Main filters arrangement
    //

    @Test
    fun noTokenAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNull(filter)
    }

    @Test
    fun noBasicAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNull(filter)
    }

    //
    // Users mappings
    //

    @Test
    @Disabled("During refactoring time, the being tested bean is part of GeneralConfiguration, so it is all the" +
            " time in ApplicationContext.")
    fun localFileTokensToUserMapperBeanNotPresentWhenSimpleTokenMethodDisabled() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            this.ctx.getBean(TokenToUserMapper::class.java)
        }
    }

    //
    // Requests against MockMVC
    //

    @Test
    fun authenticatedAsAnonymousUserWhenBasicAuthUsedButNoMethodEnabled() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders
                                .get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT)
                                .with(httpBasic(
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                        FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                                )
                )
                .andReturn().response

        // ASSERT
        assertEquals("anonymousUser", response.contentAsString)
    }

    @Test
    fun authenticatedAsAnonymousUserWhenTokenUsedButNoMethodEnabled() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME, FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        assertEquals("anonymousUser", response.contentAsString)
    }

    @Test
    fun authenticatedAsAnonymousUserWhenNoAnyCredentialsUsedAndNoMethodEnabled() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT))
                .andReturn().response

        // ASSERT
        assertEquals("anonymousUser", response.contentAsString)
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_NO_METHOD])
    class TestConfiguration: FakeControllerConfiguration()

}

