package nsl.sam.method.token.tokenssource.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokenFileImporter
import nsl.sam.method.token.tokenssource.TokensSource
import nsl.sam.utils.prune
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import javax.annotation.PostConstruct

class InMemoryTokensSource: TokensSource {

    companion object {
        val log by logger()
    }

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    private val tokensMap: MutableMap<String, ResolvedToken> = mutableMapOf()

    fun getResolvedToken(tokenAsString: String): ResolvedToken {

        if (tokensMap.containsKey(tokenAsString))
            return tokensMap[tokenAsString]!!

        throw BadCredentialsException("No token ${tokenAsString.prune(5)} in local file")
    }

    @PostConstruct
    fun initialize() {
        if (tokensFilePath.isNotBlank()) importTokensFromFile()
    } // init()

    private fun importTokensFromFile() {
        TokenFileImporter(tokensFilePath).use { tokensImporter ->

            log.debug("Importing tokens from file")

            for (token in tokensImporter) {
                log.debug("Token from parser: ${token}")
                tokensMap.put(token.tokenValue, token)
            }
        }
    }
}