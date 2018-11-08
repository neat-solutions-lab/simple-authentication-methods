package nsl.sam.functional.anonymousfallback.annotation

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/functional-test/config/effectively-empty-passwords.conf",
    "nsl.sam.tokens-file=src/functional-test/config/effectively-empty-tokens.conf",
    "server.address=localhost"
])
class AnnotationEnabledFallbackMoteFT {

    @Autowired
    private lateinit var mvc: MockMvc


    @Test
    fun userInfoEndpointReportsAnonymousUserWhenNoCredentialsAtAll() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT))
                .andReturn().response

        println("response: ${response.contentAsString}")

        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo("anonymousUser")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([
        AuthenticationMethod.SIMPLE_TOKEN,
        AuthenticationMethod.SIMPLE_BASIC_AUTH
    ], localAnonymousFallback = true)
    class TestConfiguration : FakeControllerConfiguration()
}