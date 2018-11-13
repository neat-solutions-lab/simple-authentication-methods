package nsl.sam.functional.principal.token

import nsl.sam.FunctionalTestConstants
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
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [AnonymousTokenPrincipalFTConfiguration::class]
)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "nsl.sam.tokens-file=src/functional-test/config/tokens.conf"])
class AnonymousTokenPrincipalFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun userAuthenticatedAsValidUserFromTokensFileEvenThoughAnonymousAccessIsAllowed() {
        val response: MockHttpServletResponse = mvc.perform(
                MockMvcRequestBuilders.get("/principal-aware-endpoint")
                        .header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
        ).andReturn().response

        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("User is tester")
        println("response: ${response.contentAsString}")
    }

    @Test
    fun accessDeniedWhenWrongTokenUsedEvenThoughAnonymousAccessIsAllowed() {
        val response: MockHttpServletResponse = mvc.perform(
                MockMvcRequestBuilders.get("/principal-aware-endpoint")
                        .header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE
                        )
        ).andReturn().response

        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        println("response: ${response.contentAsString}")

    }

    @Test
    fun userAuthenticatedAsAnonymousWhenNoCredentialsProvidedAndAnonymousAccessAllowed() {
        val response: MockHttpServletResponse = mvc.perform(
                MockMvcRequestBuilders.get("/principal-aware-endpoint")
        ).andReturn().response

        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("User is anonymousUser")
        println("response: ${response.contentAsString}")

    }


}

@Configuration
@EnableSimpleAuthenticationMethods(authorizations = "antMatchers('/*').permitAll()")
class AnonymousTokenPrincipalFTConfiguration {
    @Bean
    fun principalAwareController() = PrincipalAwareController()
}