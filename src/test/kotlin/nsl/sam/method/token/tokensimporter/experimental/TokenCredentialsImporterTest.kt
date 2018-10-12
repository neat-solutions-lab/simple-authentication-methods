package nsl.sam.method.token.tokensimporter.experimental

import nsl.sam.method.token.tokensimporter.experimental.reader.FileTokensReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class TokenCredentialsImporterTest {

    @Test
    fun fileWithOneTokenProcessedSuccessfully() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(1)
    }

    @Test
    fun fileWithTenTokensProcessedSuccessfully() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/ten-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(10)
    }

    @Test
    fun corruptedTokensOmittedSilently() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/corrupted-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(3)
    }

    @Test
    fun noErrorsAndTokensImportedWhenFileWithOnlyCommentsProcessed() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/only-comments-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(0)
    }

    @Test
    fun noErrorsAndTokensImportedWhenEmptyProcessed() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/empty-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(0)
    }

    @Test
    fun noErrorsAndAllTokensPassedThroughWhenFileWithDuplicatesProcessed() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/duplicate-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(6)
    }

    @Test
    fun allTokensImportedFromFileWithMultipleTokensAndComments() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/multiple-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(10)
    }

    @Test
    fun noErrorsAndTokensWhenNoExistingPathUsed() {
        val tokenCredentialsImporter =
                TokenCredentialsImporter(FileTokensReader("src/test/config/absent-tokens.conf"))

        var tokensCounter = 0

        tokenCredentialsImporter.reset()
        tokenCredentialsImporter.use {
            for (token in it) {
                println("token: $token")
                tokensCounter++
            }
        }
        Assertions.assertThat(tokensCounter).isEqualTo(0)
    }
}