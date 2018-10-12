package nsl.sam.method.token.tokensresolver.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporter
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.utils.prune
import org.springframework.security.authentication.BadCredentialsException
import javax.annotation.PostConstruct

class InMemoryTokensResolver(private val tokensImporter: TokenCredentialsImporter): TokensResolver {

    companion object {
        val log by logger()
    }

    private val tokensMap: MutableMap<String, ResolvedToken> = mutableMapOf()

    init {
        importTokensFromFile()
    }

    override fun getResolvedToken(tokenAsString: String): ResolvedToken {

        if (tokensMap.containsKey(tokenAsString))
            return tokensMap[tokenAsString]!!

        throw BadCredentialsException("No token ${tokenAsString.prune()} in local file")
    }

    @PostConstruct
    fun initialize() {
        importTokensFromFile()
    } // init()

    private fun importTokensFromFile() {
        tokensImporter.reset()
        tokensImporter.use { tokensImporter ->

            log.debug("Importing tokens from file")

            for (token in tokensImporter) {
                log.debug("Adding token to in-memory tokens map: $token")
                tokensMap[token.tokenValue] = token
            }
        }
    }
}