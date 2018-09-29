package nsl.sam.functional.authorization

import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
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
class CustomChainedAuthorizationTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun succeedAuthorizationToUserAreaWithBasicAuth() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/user-area")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "user",
                                        "user")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun succeedAuthorizationToUserAdminWithBasicAuth() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/admin-area")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "admin",
                                        "admin")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun forbiddenAuthorizationToUserAreaWithBasicAuth() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/user-area")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "admin",
                                        "admin")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun forbiddenAuthorizationToAdminAreaWithBasicAuth() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/admin-area")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "user",
                                        "user")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun forbiddenAuthorizationToRootWithBasicAuth() {

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "user",
                                        "user")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Configuration
    @EnableSimpleAuthenticationMethods(
            authorizations = "antMatchers('/user-area/**').hasRole('USER')" +
                             ".antMatchers('/admin-area/**').hasRole('ADMIN')")
    class TestConfiguration {
        @Bean
        fun customAuthorizationTestController() = CustomAuthorizationTestController()
    }
}