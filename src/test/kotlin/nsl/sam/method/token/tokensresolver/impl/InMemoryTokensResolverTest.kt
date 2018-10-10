package nsl.sam.method.token.tokensresolver.impl

import nsl.sam.method.token.tokensimporter.impl.FileTokensImporter
import org.junit.jupiter.api.Test

internal class InMemoryTokensResolverTest {

    @Test
    fun test() {

        val fileTokenImporter = FileTokensImporter("src/test/config/tokens.conf")
        val inMemoryTokensResolver = InMemoryTokensResolver(fileTokenImporter)

        val resolvedToken = inMemoryTokensResolver.getResolvedToken("12345")
        println("resolvedToken: $resolvedToken")

    }

}