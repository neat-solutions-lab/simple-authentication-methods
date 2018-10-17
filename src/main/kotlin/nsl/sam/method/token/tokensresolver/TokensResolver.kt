package nsl.sam.method.token.tokensresolver

import nsl.sam.method.token.domain.token.ResolvedToken

interface TokensResolver {
    fun getResolvedToken(tokenAsString: String): ResolvedToken
}
