package nsl.sam.method.token.tokensresolver

import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.method.token.domain.token.ResolvedToken

interface TokensResolver : ItemsAvailabilityAware {
    fun getResolvedToken(tokenAsString: String): ResolvedToken
}
