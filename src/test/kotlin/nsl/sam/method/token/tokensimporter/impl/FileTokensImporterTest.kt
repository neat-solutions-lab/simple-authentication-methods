package nsl.sam.method.token.tokensimporter.impl

import org.junit.jupiter.api.Test

internal class FileTokensImporterTest {

    @Test
    fun test() {
        val fileTokenImporter = FileTokensImporter("src/test/config/tokens.conf")
        fileTokenImporter.reset()
        fileTokenImporter.use {
            for (token in fileTokenImporter) {
                println("token: $token")
            }
        }
    }

}