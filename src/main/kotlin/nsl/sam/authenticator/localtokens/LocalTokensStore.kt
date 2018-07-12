package nsl.sam.authenticator.localtokens

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class LocalTokensStore {

    companion object { val log by logger() }

    @Value("\${sms.tokens-file:}")
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