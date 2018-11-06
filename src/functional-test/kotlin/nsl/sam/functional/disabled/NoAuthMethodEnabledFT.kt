package nsl.sam.functional.disabled

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.tokendetails.TokenDetailsService
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*
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

    //@Autowired
    //lateinit var webSecurityConfigurer: WebSecurityConfigurerAdapter

    @Autowired
    lateinit var filterChain: FilterChainProxy

    @Autowired
    lateinit var mvc: MockMvc

    //
    // Main beans arrangement
    //

    @Test
    fun defaultSpringBootWebSecurityConfigurerBeanPresent() {
        //val adapter = this.ctx.getBean(WebSecurityConfigurerAdapter::class.java)
        //this.ctx.beanDefinitionNames.forEach { println(it) }

        val defaultConfigurer = this.ctx.beanDefinitionNames
                .find { it.contains("SpringBootWebSecurityConfiguration\$DefaultConfigurerAdapter") }
        println("default configurer: $defaultConfigurer")
        org.assertj.core.api.Assertions.assertThat(defaultConfigurer).isNotNull()

    }

    //
    // Main filters arrangement
    //

    @Test
    fun noTokenAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNull(filter)
    }

    /*
     * default Spring Boot configuration configures Basic Auth with random password, so BasicAuthenticationFilter
     * should be present in the filters chain
     */
    @Test
    fun basicAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = this.filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        org.assertj.core.api.Assertions.assertThat(filter).isNotNull()
    }

    //
    // Users mappings
    //

    @Test
    fun localFileTokensToUserMapperBeanNotPresentWhenSimpleTokenMethodDisabled() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            this.ctx.getBean(TokenDetailsService::class.java)
        }
    }

    //
    // Requests against MockMVC
    //

    @Test
    fun failedAuthenticationDueToDefaultRandomBasicHttpPassword() {
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
                .andExpect(unauthenticated())
                .andReturn().response
        println("response: ${response.contentAsString}")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_NO_METHOD])
    class TestConfiguration : FakeControllerConfiguration()
}

