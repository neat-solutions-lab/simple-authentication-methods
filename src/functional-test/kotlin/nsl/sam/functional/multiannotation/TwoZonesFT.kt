package nsl.sam.functional.multiannotation

import nsl.sam.FunctionalTestConstants
import nsl.sam.functional.controller.TwoZonesTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.config.InstrumentedWebSecurityConfigurerTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [ZoneOneConfiguration::class, ZoneTwoConfiguration::class, TestConfiguration::class]
)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.zone-one-passwords=src/functional-test/config/passwords-zone-one.conf",
    "sam.zone-two-passwords=src/functional-test/config/passwords-zone-two.conf"])
class TwoZonesFT {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @Disabled("Only for 'manual' execution (from IDE)")
    fun printDynamicConfigurators() {
        val dynamicConfigurers = appCtx.getBeansOfType(WebSecurityConfigurerAdapter::class.java)
        dynamicConfigurers.forEach { println("instrumentation configurer: $it") }
    }

    @Test
    fun callZoneOneAsAuthorizedUser() {
        val mvcResult = mvc.perform(
                get(
                        FunctionalTestConstants.ZONE_ONE_ENTRY_POINT)
                        .with(httpBasic(
                                "zone-one-user", "zone-one-password"
                        ))).andReturn()

        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun callZoneTwoAsAuthorizedUser() {
        val mvcResult = mvc.perform(
                get(
                        FunctionalTestConstants.ZONE_TWO_ENTRY_POINT)
                        .with(httpBasic(
                                "zone-two-user", "zone-two-password"
                        ))).andReturn()

        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun callZoneOneAsZoneTwoUser() {
        val mvcResult = mvc.perform(
                get(
                        FunctionalTestConstants.ZONE_ONE_ENTRY_POINT)
                        .with(httpBasic(
                                "zone-two-user", "zone-two-password"
                        ))).andReturn()

        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun callZoneTwoAsZoneOneUser() {
        val mvcResult = mvc.perform(
                get(
                        FunctionalTestConstants.ZONE_TWO_ENTRY_POINT)
                        .with(httpBasic(
                                "zone-one-user", "zone-one-password"
                        ))).andReturn()

        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun twoDynamicConfigurersWhenTwoEnableAnnotations() {
        val dynamicConfigurers = appCtx.getBeansOfType(WebSecurityConfigurerAdapter::class.java)
        val beanNames = dynamicConfigurers.keys

        assertThat(dynamicConfigurers.size).isEqualTo(2)
        assertThat(beanNames).allMatch { it.startsWith(InstrumentedWebSecurityConfigurerTemplate::class.qualifiedName!!)}
    }

}

@Configuration
class TestConfiguration {
    @Bean
    fun twoZonedController() = TwoZonesTestController()
}

@Configuration
@EnableSimpleAuthenticationMethods(
        order = 10, methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH],
        match = FunctionalTestConstants.ZONE_ONE_MATCH)
@SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-one-passwords")
class ZoneOneConfiguration

@Configuration
@EnableSimpleAuthenticationMethods(
        order = 20, methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH],
        match = FunctionalTestConstants.ZONE_TWO_MATCH)
@SimpleBasicAuthentication(passwordsFilePropertyName = "sam.zone-two-passwords")
class ZoneTwoConfiguration
