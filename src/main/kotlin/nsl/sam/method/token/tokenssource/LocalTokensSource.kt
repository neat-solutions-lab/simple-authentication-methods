package nsl.sam.method.token.tokenssource

import nsl.sam.logger.logger
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokenFileImporter
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct

class LocalTokensSource {

    companion object {
        val log by logger()
    }

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    val tokensMap: MutableMap<String, ResolvedToken> = mutableMapOf()

    fun get(tokenAsString: String): ResolvedToken? {
        return tokensMap.get(tokenAsString)
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