package nsl.sam.functional.tokensimporter

import nsl.sam.method.token.tokensimporter.TokenCredentialsImporter
import nsl.sam.method.token.tokensimporter.reader.FileTokensReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CorruptedTokensFileFileTokensImporterFT {

    @Test
    fun noErrorsWhenFileWithCorruptedTokensIsProcessed() {

        val fileTokensImporter = TokenCredentialsImporter(FileTokensReader("src/functional-test/config/corrupted-tokens.conf"))

        var tokensCounter = 0

        fileTokensImporter.reset()
        fileTokensImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(3)
    }

}