package nsl.sam.method.token.tokensimporter.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TokenFileImporterTest {

    @Test
    fun test() {
        val fileTokenImporter = TokenFileImporter("src/test/config/tokens.conf")
        fileTokenImporter.reset()
        fileTokenImporter.use {
            for (token in fileTokenImporter) {
                println("token: $token")
            }
        }
    }

}