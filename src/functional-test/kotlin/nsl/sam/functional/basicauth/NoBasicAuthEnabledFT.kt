package nsl.sam.functional.basicauth

import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
//import nsl.sam.spring.config.DisableBasicAuthConfigurer
//import org.junit.Rule
//import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoBasicAuthEnabledFT {

    @Autowired
    private lateinit var ctx: ApplicationContext

    @Test
    fun disableBasicAuthConfigurerBeanPresent() {
        ctx.getBean(DisableBasicAuthConfig::class.java)
    }

    @Test
    fun basicAuthConfigBeanNotPresent() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            this.ctx.getBean(BasicAuthConfig::class.java)
        }
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_NO_METHOD])
    class TestConfiguration: FakeControllerConfiguration()
}
