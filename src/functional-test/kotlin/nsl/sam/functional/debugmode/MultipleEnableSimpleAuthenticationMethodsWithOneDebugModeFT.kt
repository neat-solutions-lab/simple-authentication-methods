package nsl.sam.functional.debugmode

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.EnableWebSecurityInDebugMode
import nsl.sam.spring.config.EnableWebSecurityInDefaultMode
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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MultipleEnableSimpleAuthenticationMethodsWithOneDebugModeFT {

    @Autowired
    lateinit var applicationContext: ConfigurableApplicationContext

    @Test
    fun enableWebSecurityInDebugModeBeanPresentInContext() {
        applicationContext.getBean(EnableWebSecurityInDebugMode::class.java)
    }

    @Test
    fun enableWebSecurityInDefaultModeBeanNotPresentInContext() {

        Assertions.assertThrows(NoSuchBeanDefinitionException::class.java) {
            applicationContext.getBean(EnableWebSecurityInDefaultMode::class.java)
        }
    }

    @Test
    fun webSecurityConfigurationHasDebugEnabledFieldSetToTrue() {
        val webSecurityConfiguration: WebSecurityConfiguration =
                applicationContext.getBean(WebSecurityConfiguration::class.java)

        val fieldValue = ReflectionTestUtils.getField(webSecurityConfiguration, "debugEnabled")
        assertEquals(true, fieldValue,
                "debugEnabled field in ${WebSecurityConfiguration::class.simpleName} should have value set to true.")
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
    @EnableSimpleAuthenticationMethods
    class EnableDefaultModeConfiguration

    @Configuration
    @EnableSimpleAuthenticationMethods(debug = false)
    class EnableNoDebugModeConfiguration

    @Configuration
    @EnableSimpleAuthenticationMethods(debug = true)
    class EnableDebugModeConfiguration

}