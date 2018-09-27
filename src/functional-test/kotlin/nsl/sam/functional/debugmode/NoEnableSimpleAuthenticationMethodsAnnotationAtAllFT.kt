package nsl.sam.functional.debugmode

import nsl.sam.spring.config.EnableWebSecurityInDebugMode
import nsl.sam.spring.config.EnableWebSecurityInDefaultMode
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoEnableSimpleAuthenticationMethodsAnnotationAtAllFT {

    @Autowired
    lateinit var applicationContext: ApplicationContext


    //TODO: After introduction of instrumentation configurators this test shuld pass
    @Disabled
    @Test
    fun noBeanWithEnableWebSecurityAnnotationInApplicationContext() {
        val beanNames = applicationContext.getBeanNamesForAnnotation(EnableWebSecurity::class.java)
        Assertions.assertThat(beanNames).isEmpty()
    }


    @Configuration
    class EmptyConfiguration

}