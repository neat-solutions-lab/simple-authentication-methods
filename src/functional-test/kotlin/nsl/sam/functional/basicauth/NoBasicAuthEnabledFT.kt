package nsl.sam.functional.basicauth

import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import org.junit.Ignore
//import nsl.sam.spring.config.DisableBasicAuthConfigurer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull
import org.junit.rules.ExpectedException
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.annotation.Configuration


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [TestConfiguration::class])
@AutoConfigureMockMvc
class NoBasicAuthEnabledFT {

    @get:Rule
    var thrown = ExpectedException.none()

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Test
    fun disableBasicAuthConfigurerBeanPresent() {
        ctx.getBean(DisableBasicAuthConfig::class.java)
    }

    @Test
    fun basicAuthConfigBeanNotPresent() {
        this.thrown.expect(NoSuchBeanDefinitionException::class.java)
        this.ctx.getBean(BasicAuthConfig::class.java)
    }

}

@Configuration
@EnableSimpleAuthenticationMethods([])
class TestConfiguration: FakeControllerConfiguration()