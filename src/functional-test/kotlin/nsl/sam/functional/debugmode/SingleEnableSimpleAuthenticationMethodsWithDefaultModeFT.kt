package nsl.sam.functional.debugmode

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.EnableWebSecurityInDebugMode
import nsl.sam.spring.config.EnableWebSecurityInDefaultMode
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SingleEnableSimpleAuthenticationMethodsWithDefaultModeFT {

    @Autowired
    lateinit var applicationContext: ConfigurableApplicationContext

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun enableWebSecurityInDebugModeBeanNotPresentInContext() {
        thrown.expect(NoSuchBeanDefinitionException::class.java)
        applicationContext.getBean(EnableWebSecurityInDebugMode::class.java)
    }

    @Test
    fun enableWebSecurityInDefaultModeBeanPresentInContext() {
        applicationContext.getBean(EnableWebSecurityInDefaultMode::class.java)
    }

    @Test
    fun webSecurityConfigurationHasDebugEnabledFieldSetToFalse() {
        val webSecurityConfiguration: WebSecurityConfiguration =
                applicationContext.getBean(WebSecurityConfiguration::class.java)

        val fieldValue = ReflectionTestUtils.getField(webSecurityConfiguration, "debugEnabled")
        assertEquals(false, fieldValue,
                "debugEnabled field in ${WebSecurityConfiguration::class.simpleName} should have value set to false.")
    }

    @Test
    fun noThirdPartyBeanWithEnableWebSecurityAnnotationInApplicationContext() {

        val beanNames = applicationContext.getBeanNamesForAnnotation(EnableWebSecurity::class.java)
        val filteredBeanNames = beanNames.filter {
            (it != EnableWebSecurityInDefaultMode::class.qualifiedName) &&
                    (it != EnableWebSecurityInDebugMode::class.qualifiedName)
        }
        Assertions.assertThat(filteredBeanNames).isEmpty()
    }


    @Configuration
    @EnableSimpleAuthenticationMethods
    class EnableDefaultModeConfiguration

}