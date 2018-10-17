package nsl.sam.method.token.tokensimporter.parser

import nsl.sam.importer.parser.CredentialsParser
import nsl.sam.importer.extractor.CredentialsParsingException
import nsl.sam.logger.logger
import nsl.sam.method.token.domain.token.ResolvedToken

class TokensParser : CredentialsParser<ResolvedToken> {

    companion object {
        val log by logger()
    }

    override fun parse(credentialsLine: String): ResolvedToken {
        val parts: List<String> = credentialsLine.trim().split(Regex("\\s+"))
        if (parts.size < 2) throw CredentialsParsingException("Wrong format of tokens file.")

        log.debug("parts number: ${parts.size}")

        val tokenValue = parts[0]
        val userName = parts[1]

        val roles = if (parts.size > 2) parts.subList(2, parts.lastIndex + 1) else emptyList()

        return ResolvedToken(
                tokenValue, userName, roles.map { "ROLE_$it" }.toTypedArray()
        )
    }
}