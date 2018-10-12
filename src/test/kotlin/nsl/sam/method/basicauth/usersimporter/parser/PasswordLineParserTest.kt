package nsl.sam.method.basicauth.usersimporter.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PasswordLineParserTest {

    @Test
    fun testHappyPath() {
        val credentialsTriple = PasswordLineParser.parseToTriple("user:{noop}pass USER ADMIN")
        println("credentialsTriple: $credentialsTriple")
    }

    @Test
    fun test() {
        val credentialsTriple = PasswordLineParser.parseToTriple("user:")
        println("credentialsTriple: $credentialsTriple")
        println("credentialsTriple.third: ${credentialsTriple.third.size}")
    }

}