package nsl.sam.functional.authorization

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CustomPermissionEvaluatorFTConfiguration::class])
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/functional-test/config/passwords.conf"])
class CustomPermissionEvaluatorFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun successfulAccessWhenGuardBeanAllowsAccess() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/allowed")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "test",
                                        "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("Allowed endpoint!")
    }

    @Test
    fun failedAccessWhenGuardDeniesAccess() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/disallowed")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "test",
                                        "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun test() {
        println("Hello")
    }


}

@Configuration
@EnableSimpleAuthenticationMethods(authorizations =
"antMatchers('/allowed/**').access('@accessGuard.isAllowed(true)')." +
"antMatchers('/disallowed/**').access('@accessGuard.isAllowed(false)')"
)
class CustomPermissionEvaluatorFTConfiguration {

    @Bean
    fun accessGuard(): CustomAccessGuardBean = CustomAccessGuardBean()

    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()

}


class CustomAccessGuardBean {
    fun isAllowed(verdict: Boolean) = verdict
}
