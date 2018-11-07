package nsl.test.changes

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("exploratory")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [PasswordsFileChangesDetectorExplorationConfiguration::class])
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=/tmp/passwords.conf",
    "sam.detect-passwords-file-changes=true"
])
class PasswordsFileChangesDetectorExploration {

    companion object {
        const val SLEEP_TIME = 100000L
    }

    @Test
    fun test() {
        println("Hello from FileChangesDetectorExploration")
        Thread.sleep(SLEEP_TIME)
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication
class PasswordsFileChangesDetectorExplorationConfiguration