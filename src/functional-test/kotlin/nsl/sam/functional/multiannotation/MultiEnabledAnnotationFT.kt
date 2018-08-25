package nsl.sam.functional.multiannotation

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.ClassUtils

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MultiEnabledAnnotationFT {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun twoBeansAnnotatedWithEnableSimpleAuthenticationMethods() {
        val beanTypes = applicationContext.getBeanNamesForAnnotation(
                        EnableSimpleAuthenticationMethods::class.java).map {
            ClassUtils.getUserClass(applicationContext.getType(it!!)!!)
        }
        assertThat(beanTypes).containsExactlyInAnyOrder(
                FirstEnablingConfiguration::class.java, SecondEnablingConfiguration::class.java)
    }

    @Test
    fun twoDynamicallyCreatedWebSecurityConfigurers() {
        // TODO: Finish when dynamic configurers start working properly.
    }

    @Configuration
    @EnableSimpleAuthenticationMethods(debug = false)
    class FirstEnablingConfiguration

    @Configuration
    @EnableSimpleAuthenticationMethods(debug = true)
    class SecondEnablingConfiguration
}