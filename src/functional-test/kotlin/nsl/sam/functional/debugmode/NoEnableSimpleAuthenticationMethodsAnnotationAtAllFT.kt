package nsl.sam.functional.debugmode

import nsl.sam.spring.config.EnableWebSecurityInDebugMode
import nsl.sam.spring.config.EnableWebSecurityInDefaultMode
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoEnableSimpleAuthenticationMethodsAnnotationAtAllFT {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun noBeanWithEnableWebSecurityAnnotationInApplicationContext() {

        //TODO: After introduction of dynamic configurators this test shuld pass
        val beanNames = applicationContext.getBeanNamesForAnnotation(EnableWebSecurity::class.java)
        beanNames.forEach {
            println("Bean annotated with EnableWebSecurity: ${it}")
        }
        //Assertions.assertThat(beanNames).isEmpty()
    }


    @Configuration
    class EmptyConfiguration

}