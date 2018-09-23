package nsl.sam.functional.multiannotation

import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [ZoneOneConfiguration::class, ZoneTwoConfiguration::class]
)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.zone-one-passwords=src/functional-test/config/passwords-zone-one.conf",
    "sam.zone-two-passwords=src/functional-test/config/passwords-zone-two.conf"])
class TwoZonesTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Test
    fun contextLoaded() {

        appCtx.beanDefinitionNames.forEach {
            println(it)
        }

    }

}

@Configuration
@EnableSimpleAuthenticationMethods(order = 10, methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH], match = "/zone-one/**")
@SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-one-passwords")
class ZoneOneConfiguration

@Configuration
@EnableSimpleAuthenticationMethods(order = 20, methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH], match = "/zone-two/**")
@SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-two-passwords")
class ZoneTwoConfiguration
