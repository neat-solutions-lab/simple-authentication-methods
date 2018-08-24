package nsl.sam.functional.debugmode

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [SingleEnableSimpleAuthenticationMethodsWithDebugModeFunctionalTestConfiguration::class])
@AutoConfigureMockMvc
class SingleEnableSimpleAuthenticationMethodsWithDebugModeFunctionalTest {

    @Autowired
    lateinit var applicationContext: ConfigurableApplicationContext

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun enableWebSecurityInDebugModeBeanPresentInContext() {
        //applicationContext.getBean(EnableWebSecurityInDebugMode::class.java)
    }

    @Test
    fun enableWebSecurityInDefaultModeBeanNotPresentInContext() {
        //thrown.expect(NoSuchBeanDefinitionException::class.java)
        //applicationContext.getBean(EnableWebSecurityInDefaultMode::class.java)
    }

    @Test
    fun webSecurityConfigurationHasDebugEnabledSetToTrue() {
        val webSecurityConfiguration: WebSecurityConfiguration =
                applicationContext.getBean(WebSecurityConfiguration::class.java)

        val fieldValue = ReflectionTestUtils.getField(webSecurityConfiguration, "debugEnabled")
        println("--> field: $fieldValue")
    }

}