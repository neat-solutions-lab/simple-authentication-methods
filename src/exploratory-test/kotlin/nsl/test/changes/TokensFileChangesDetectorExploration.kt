package nsl.test.changes

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
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
        classes = [TokensFileChangesDetectorExplorationConfiguration::class])
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "nsl.sam.tokens-file=/tmp/tokens.conf",
    "sam.detect-tokens-file-changes=true",
    "sam.tokens-file-change-detection-period=10"
])
class TokensFileChangesDetectorExploration {

    companion object {
        const val SLEEP_TIME = 100000L
    }

    @Test
    fun test() {
        println("Hello from ${this::class.simpleName}")
        Thread.sleep(SLEEP_TIME)
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(detectTokensFileChanges = false)
class TokensFileChangesDetectorExplorationConfiguration