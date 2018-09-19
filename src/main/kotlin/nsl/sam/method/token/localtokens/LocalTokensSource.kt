package nsl.sam.method.token.localtokens

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct

class LocalTokensSource {

    companion object { val log by logger() }

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    val tokensMap:MutableMap<String, LocalToken> = mutableMapOf()

    fun get(tokenAsString:String) : LocalToken? {
        return tokensMap.get(tokenAsString)
    }

    @PostConstruct
    fun init() {
        if (tokensFilePath.isNotBlank()) importTokensFromFile()
    } // init()

    private fun importTokensFromFile() {
        TokenFileImporter(tokensFilePath).use { tokensImporter ->

            log.debug("Importing tokens from file")

            for(token in tokensImporter) {
                log.debug("Token from parser: ${token}")
                tokensMap.put(token.tokenValue, token)
            }
        }
    }
}