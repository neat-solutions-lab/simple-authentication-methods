package nsl.sam.functional.anonymousfallback.basicauth

import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/effectively-empty-passwords.conf",
    "nsl.sam.anonymous-fallback=true",
    "server.address=localhost"
])
class EmptyPassFileAndFallbackFT {

    @Test
    fun test() {
        println("Hello")
    }

    @Configuration
    @EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_BASIC_AUTH])
    class TestConfiguration : FakeControllerConfiguration()

}