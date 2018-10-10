package nsl.sam.method.token.tokenssource.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokenFileImporter
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct

class InMemoryTokensSource {

    companion object {
        val log by logger()
    }

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    private val tokensMap: MutableMap<String, ResolvedToken> = mutableMapOf()

    fun getResolvedToken(tokenAsString: String): ResolvedToken? {
        return tokensMap[tokenAsString]
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