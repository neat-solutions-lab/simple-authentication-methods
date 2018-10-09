package nsl.sam.functional.basicauth

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Property defining location of passwords file is present, but the file itself is absent.
 * <p>Consequences:
 *  <ul>
 *      <li>http basic auth disabled (how to check it?)</li>
 *      <li>DisableBasicAuthConfigurer bean present in ApplicationContext</li>
 *      <li>DefaultUserDetailsService bean not present in ApplicationContext</li>
 *      <li>BasicAuthConfiguration bean not present in ApplicationContext</li>
 *  </ul>
 */
//TODO: Finish this test after instrumentation web configurators are finished.
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoPasswordsFileFT {

    @Autowired
    lateinit var applicationContext: ApplicationContext

//    @Test
//    fun basicAuthConfigNotPresent() {
//        val basicAuthConfig = ctx.getBean(BasicAuthConfiguration::class.java)
//        assertNull(basicAuthConfig)
//    }


    @Test
    fun noBasicAuthWhenNoPasswordsFile() {
    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    class TestConfiguration : FakeControllerConfiguration()

}