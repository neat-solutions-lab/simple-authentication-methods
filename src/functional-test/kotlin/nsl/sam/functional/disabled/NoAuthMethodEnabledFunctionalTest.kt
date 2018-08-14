package nsl.sam.functional.disabled

import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import nsl.sam.spring.config.TokenAuthConfig
import nsl.sam.spring.config.SimpleWebSecurityConfigurer
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [NoAuthMethodEnabledFunctionalTestConfig::class])
@AutoConfigureMockMvc
class NoAuthMethodEnabledFunctionalTest {
    @get:Rule
    var thrown: ExpectedException = ExpectedException.none()

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Autowired
    lateinit var webSecurityConfigurer: WebSecurityConfigurerAdapter

    @Autowired
    lateinit var filterChain: FilterChainProxy

    @Test
    fun fakeUserDetailsServiceActive() {
        val userDetailsService = webSecurityConfigurer.userDetailsServiceBean()
        thrown.expect(UsernameNotFoundException::class.java)
        thrown.expectMessage("Fake UserDetailsService doesn't provide any users.")
        userDetailsService.loadUserByUsername("fake")
    }

    @Test
    fun noTokenAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = filterChain.getFilters("/").find { it::class == TokenAuthenticationFilter::class }
        assertNull(filter)
    }

    @Test
    fun noBasicAuthenticationFilterInFilterChainWhenNoMethodIsEnabled() {
        val filter = filterChain.getFilters("/").find { it::class == BasicAuthenticationFilter::class }
        assertNull(filter)
    }

    @Test
    fun disableBasicAuthConfigBeanPresent() {
        ctx.getBean(DisableBasicAuthConfig::class.java)
    }

    @Test
    fun webSecurityConfigurerBeanPresent() {
        this.ctx.getBean(SimpleWebSecurityConfigurer::class.java)
    }

    @Test
    fun basicAuthConfigBeanNotPresent() {
        this.thrown.expect(NoSuchBeanDefinitionException::class.java)
        this.ctx.getBean(BasicAuthConfig::class.java)
    }

    @Test
    fun tokenAuthConfigBeanNotPresent() {
        this.thrown.expect(NoSuchBeanDefinitionException::class.java)
        this.ctx.getBean(TokenAuthConfig::class.java)
    }

}