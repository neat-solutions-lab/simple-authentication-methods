package nsl.sam.method.token.tokensresolver.impl

import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.importer.reader.impl.FileCredentialsReader
import org.junit.jupiter.api.Test
import java.util.*

internal class InMemoryTokensResolverTest {

    @Test
    fun test() {

        val fileTokenImporter = TokensCredentialsImporter(FileCredentialsReader("src/test/config/tokens.conf"))
        val inMemoryTokensResolver = InMemoryTokensResolver.createInstance(fileTokenImporter, Properties())

        val resolvedToken = inMemoryTokensResolver.getResolvedToken("12345")
        println("resolvedToken: $resolvedToken")

    }

}