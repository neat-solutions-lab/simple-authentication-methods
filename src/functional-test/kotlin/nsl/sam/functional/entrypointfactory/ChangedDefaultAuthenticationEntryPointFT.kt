package nsl.sam.functional.entrypointfactory

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
class ChangedDefaultAuthenticationEntryPointFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun responseFromChangedDefaultAuthenticationEntryPointWhenRequestWithNoCredentialsToProtectedUrl() {
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/user-area")
                )
                .andReturn().response

        println(response.contentAsString)

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("Response from ${FirstTestTimeEntryPoint::class.qualifiedName}")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods(authenticationEntryPointFactory = [FirstTestTimeEntryPointFactory::class])
    class TestConfiguration {
        @Bean
        fun customAuthorizationTestController() = CustomAuthorizationTestController()
    }
}