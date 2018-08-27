package nsl.sam.functional.basicauth

import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.spring.config.BasicAuthConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Property defining location of passwords file is present, but the file itself is absent.
 * <p>Consequences:
 *  <ul>
 *      <li>http basic auth disabled (how to check it?)</li>
 *      <li>DisableBasicAuthConfigurer bean present in ApplicationContext</li>
 *      <li>LocalUserDetailsService bean not present in ApplicationContext</li>
 *      <li>BasicAuthConfig bean not present in ApplicationContext</li>
 *  </ul>
 */
//TODO: Finish this test after dynamic web configurators are finished.
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoPasswordsFileFT {

    @Autowired
    lateinit var applicationContext: ApplicationContext

//    @Test
//    fun basicAuthConfigNotPresent() {
//        val basicAuthConfig = ctx.getBean(BasicAuthConfig::class.java)
//        assertNull(basicAuthConfig)
//    }


    @Test
    fun noBasicAuthWhenNoPasswordsFile() {
    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    class TestConfiguration : FakeControllerConfiguration()

}