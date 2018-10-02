package nsl.sam.functional.debugmode

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.config.EnableWebSecurityInDebugMode
import nsl.sam.core.config.EnableWebSecurityInDefaultMode
import org.junit.jupiter.api.Assertions
import org.assertj.core.api.Assertions as Assertj
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SingleEnableSimpleAuthenticationMethodsWithNoDebugModeFT {

    @Autowired
    lateinit var applicationContext: ConfigurableApplicationContext

    @Test
    fun enableWebSecurityInDebugModeBeanNotPresentInContext() {
        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java){
            applicationContext.getBean(EnableWebSecurityInDebugMode::class.java)
        }
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
        Assertj.assertThat(filteredBeanNames).isEmpty()
    }

    @Configuration
    @EnableSimpleAuthenticationMethods(debug = false)
    class EnableNoDebugModeConfiguration
}