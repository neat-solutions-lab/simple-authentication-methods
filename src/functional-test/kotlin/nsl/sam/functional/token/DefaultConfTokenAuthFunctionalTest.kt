package nsl.sam.functional.token

import nsl.sam.FunctionalTestConstants
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
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
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [DefaultConfTokenAuthFunctionalTestConfig::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class DefaultConfTokenAuthFunctionalTest {

    companion object { val log by logger() }

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var filterChain: FilterChainProxy

    @get:Rule
    var thrown = ExpectedException.none()

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Test
    fun disableBasicAuthConfigBeanNotPresentWhenBasicAuthIsNotDisabled() {
        thrown.expect(NoSuchBeanDefinitionException::class.java)
        this.ctx.getBean(DisableBasicAuthConfig::class.java)
    }

    @Test
    fun basicAuthConfigBeanPresent() {
        this.ctx.getBean(BasicAuthConfig::class.java)
    }

    @Test
    fun tokenAuthConfigBeanPresent() {
        this.ctx.getBean(TokenAuthConfig::class.java)
    }

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

    @Test
    fun successAuthenticationWithTokenAuth() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
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
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
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
                .perform(MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT))
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
