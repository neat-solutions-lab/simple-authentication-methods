package nsl.sam.functional.multiannotation

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [
            AutoOrderingFT.ZoneOneConfiguration::class,
            AutoOrderingFT.ZoneTwoConfiguration::class
        ]
)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.zone-one-passwords=src/functional-test/config/passwords-zone-one.conf",
    "sam.zone-two-passwords=src/functional-test/config/passwords-zone-two.conf"])
class AutoOrderingFT {

    @Test
    fun configurationLoadedWhenNoExplicitOrderingOnEnableAnnotationsDefined() {

    }

    @Configuration
    @EnableSimpleAuthenticationMethods(
            methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH],
            match = FunctionalTestConstants.ZONE_ONE_MATCH)
    @SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-one-passwords")
    class ZoneOneConfiguration

    @Configuration
    @EnableSimpleAuthenticationMethods(
            methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH],
            match = FunctionalTestConstants.ZONE_TWO_MATCH)
    @SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-two-passwords")
    class ZoneTwoConfiguration
}

