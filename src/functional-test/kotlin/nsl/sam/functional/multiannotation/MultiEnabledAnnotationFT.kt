package nsl.sam.functional.multiannotation

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.ClassUtils

@ExtendWith(SpringExtension::class)
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
    @Disabled
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