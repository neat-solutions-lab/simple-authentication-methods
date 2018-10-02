package nsl.sam.functional.entrypointfactory

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf"])
class SpecificEntryPointFactoryFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun responseFromCustomEntryPointWhenWrongCredentials() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/user-area")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "wrong",
                                        "wrong")
                                )
                )
                .andReturn().response

        println(response.contentAsString)

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("Response from ${TestTimeEntryPoint::class.qualifiedName}")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    @SimpleBasicAuthentication(authenticationEntryPointFactory = [TestTimeEntryPointFactory::class])
    class TestConfiguration {
        @Bean
        fun customAuthorizationTestController() = CustomAuthorizationTestController()
    }
}