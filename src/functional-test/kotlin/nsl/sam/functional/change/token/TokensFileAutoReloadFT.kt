package nsl.sam.functional.change.token

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.scheduler.ScheduledExecutor
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.io.File

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [TokensFileAutoReloadFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "sam.detect-tokens-file-changes=true"
])
class TokensFileAutoReloadFT {

    companion object {

        var tmpConfigFile: File? = null

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            tmpConfigFile = createTempFile()
            File("src/functional-test/config/tokens.conf").copyTo(tmpConfigFile!!, true)
            System.setProperty("nsl.sam.tokens-file", tmpConfigFile?.absolutePath)
            System.setProperty("nsl.sam.tokens-file-change-detection-period", "10")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            System.clearProperty("nsl.sam.tokens-file")
            System.clearProperty("nsl.sam.tokens-file-change-detection-period")
            //tmpConfigFile?.delete()

            ScheduledExecutor.shutdownNow()
        }
    }


    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun successAuthenticationWithTokenAddedInRuntime() {
        // ACT
        tmpConfigFile?.appendText("\nTOKENADDEDINRUNTIME tester USER ADMIN ROOT")

        Thread.sleep(500)

        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                "Bearer TOKENADDEDINRUNTIME"
                        )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun successAuthenticationWithNotAddedButExistingToken() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_AUTHORIZED_VALUE
                        )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithWrongTokenAfterOtherTokenAddedInRuntime() {
        // ACT
        tmpConfigFile?.appendText("\nANOTHERTOKENADDEDINRUNTIME tester USER ADMIN ROOT")

        Thread.sleep(500)

        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT).header(
                                FunctionalTestConstants.TOKEN_AUTH_HEADER_NAME,
                                "Bearer COMPLETLYWRONGTOKEN"
                        )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_TOKEN])
class TokensFileAutoReloadFTConfiguration : FakeControllerConfiguration()