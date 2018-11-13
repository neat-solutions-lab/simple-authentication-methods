package nsl.sam.functional.principal.basicauth

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.PrincipalAwareController
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
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [AuthenticatedBasicAuthPrincipalFTConfiguration::class]
)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/functional-test/config/passwords.conf"])
class AuthenticatedBasicAuthPrincipalFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun controllerProperlyRecognizesAndReportsPrincipalNameWhenValidBasicAuthCredentialsUsed() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get("/principal-aware-endpoint")
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(
                                        "test",
                                        "test")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("User is test")
        println("response: ${response.contentAsString}")
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
class AuthenticatedBasicAuthPrincipalFTConfiguration {

    @Bean
    fun principalAwareController(): PrincipalAwareController {
        return PrincipalAwareController()
    }

}