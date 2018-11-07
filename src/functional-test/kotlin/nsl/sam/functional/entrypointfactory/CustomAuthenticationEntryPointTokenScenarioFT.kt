package nsl.sam.functional.entrypointfactory

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.hamcrest.Matchers.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "nsl.sam.tokens-file=src/functional-test/config/tokens.conf"])
class CustomAuthenticationEntryPointTokenScenarioFT {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun failedAuthorizationHandledByCustomAuthenticationEntryPointAssignedToTokenMethod() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.contentAsString).isEqualTo(
                "Response from ${FirstTestTimeEntryPoint::class.qualifiedName!!}")

    }

    @Test
    fun failedAuthorizationHandledDefaultAuthenticationEntryPointWhenNoTokenInRequest() {

        val mvcResult = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                )

        val response = mvcResult.andReturn().response

        // ASSERT
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())

        /*
         * the default AuthenticationEntryPoint returns JSON with some specific fields,
         * which are examined below
         */
        mvcResult.andExpect(jsonPath("$.error", `is`("Unauthorized")))
        mvcResult.andExpect(jsonPath("$.success", `is`(false)))
        mvcResult.andExpect(jsonPath("$.message", `is`("Authentication failed")))

    }


    @Configuration
    @EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_TOKEN])
    @SimpleTokenAuthentication(authenticationEntryPointFactory = [FirstTestTimeEntryPointFactory::class])
    class CustomAuthenticationEntryPointTokenScenarioFTConfiguration : FakeControllerConfiguration()
}