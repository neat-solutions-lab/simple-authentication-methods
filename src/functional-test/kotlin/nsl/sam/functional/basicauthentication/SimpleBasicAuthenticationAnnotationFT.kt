package nsl.sam.functional.basicauthentication

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.configuration.FakeControllerConfiguration
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(properties = [
    "sam.passwords-file=src/functional-test/config/passwords.conf",
    "sam.tokens-file=src/functional-test/config/tokens.conf"])
class SimpleBasicAuthenticationAnnotationFT {

    @Test
    fun test() {

    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    @SimpleBasicAuthentication
    class TestConfiguration : FakeControllerConfiguration()

}
