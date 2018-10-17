package nsl.sam.functional.tokensresolver

import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.token.tokensresolver.impl.InMemoryTokensResolver
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DuplicateTokensInMemoryTokensResolverFT {

    @Test
    fun inCaseOfDuplicateTokensTheLastOneWins() {
        val fileTokensImporter = TokensCredentialsImporter(FileCredentialsReader("src/functional-test/config/duplicate-tokens.conf"))
        val inMemoryTokensResolver = InMemoryTokensResolver(fileTokensImporter)
        val resolvedToken = inMemoryTokensResolver.getResolvedToken("1234567891")

        Assertions.assertThat(resolvedToken.userName).isEqualTo("tester5")
    }
}