package nsl.sam.method.token.tokensresolver.impl

import nsl.sam.method.token.tokensimporter.impl.TokenFileImporter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class InMemoryTokensResolverTest {

    @Test
    fun test() {

        val fileTokenImporter = TokenFileImporter("src/test/config/tokens.conf")
        val inMemoryTokensResolver = InMemoryTokensResolver(fileTokenImporter)

        val resolvedToken = inMemoryTokensResolver.getResolvedToken("12345")
        println("resolvedToken: $resolvedToken")

    }

}