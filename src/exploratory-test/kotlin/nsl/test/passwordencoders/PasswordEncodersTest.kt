package nsl.test.passwordencoders

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.factory.PasswordEncoderFactories

@Tag("exploratory")
class PasswordEncodersTest {

    @Test
    fun passwordEncoderTest() {

        val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

        println("noop encoder example: " + passwordEncoder.encode("password"))

        println("matching result: ${passwordEncoder.matches("password", "{noop}password")}")

        val matchingResult1 = passwordEncoder.matches("password",
                "{bcrypt}\$2a\$10\$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
        println("matching result: $matchingResult1")

        val matchingResult2 = passwordEncoder.matches("password",
                "{bcrypt}\$2a\$10\$ugr6SreVsz/WjiFuUvHmr.TAWNjzvAM3jKLEhz2jjl8jqhPFNyet2")
        println("matching result: $matchingResult2")
    }

}