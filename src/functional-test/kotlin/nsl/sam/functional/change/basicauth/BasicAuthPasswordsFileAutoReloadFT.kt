package nsl.sam.functional.change.basicauth

import nsl.sam.FunctionalTestConstants
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.io.File

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [BasicAuthPasswordsFileAutoReloadFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "sam.detect-passwords-file-changes=true"
])
class BasicAuthPasswordsFileAutoReloadFT {

    companion object {

        var tmpConfigFile: File?  = null

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            tmpConfigFile = createTempFile()
            File("src/functional-test/config/passwords.conf").copyTo(tmpConfigFile!!, true)
            System.setProperty("sam.passwords-file", tmpConfigFile?.absolutePath)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            System.clearProperty("sam.passwords-file")
            tmpConfigFile?.delete()
        }
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun successfullAuhorizationOfUserAddedToPasswordsFileInRuntime() {

        tmpConfigFile?.appendText("\nadded-user:{noop}added-password ADMIN")

        Thread.sleep(5000)

        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                "added-user",
                                                "added-password")
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun successAuthenticationWithNotAddedButExistingUser() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(
                                        SecurityMockMvcRequestPostProcessors.httpBasic(
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_NAME,
                                                FunctionalTestConstants.EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD)
                                )
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.contentAsString).isEqualTo(FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY)
    }

    @Test
    fun failedAuthenticationWithBasicAuthWhenNotExistingUser() {
        // ACT
        val response: MockHttpServletResponse = mvc
                .perform(
                        MockMvcRequestBuilders.get(FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic(FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_NAME, FunctionalTestConstants.NOT_EXISTING_BASIC_AUTH_USER_PASSWORD))
                )
                .andReturn().response

        // ASSERT
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}


@Configuration
@EnableSimpleAuthenticationMethods
class BasicAuthPasswordsFileAutoReloadFTConfiguration : FakeControllerConfiguration()