package nsl.sam.method.basicauth.usersimporter.parser

import nsl.sam.importer.parser.CredentialsParser
import nsl.sam.importer.extractor.CredentialsParsingException
import nsl.sam.method.basicauth.domain.user.UserTraits

class PasswordsParser : CredentialsParser<UserTraits> {

    companion object {
        const val SYNTAX_ERROR_MESSAGE =
                "Could not parse passed string line into Triple with user, password and roles."
    }

    override fun parse(line: String): UserTraits {

        if (line.isBlank())
            throw CredentialsParsingException(SYNTAX_ERROR_MESSAGE)

        val lineParts: List<String> = line.trim().split(Regex("\\s+"))
        if (lineParts.isEmpty())
            throw CredentialsParsingException(SYNTAX_ERROR_MESSAGE)

        val userAndPassword: List<String> = lineParts[0].split(":")
        if (userAndPassword.size != 2)
            throw CredentialsParsingException(SYNTAX_ERROR_MESSAGE)

        val user = userAndPassword[0].trim()
        val pass = userAndPassword[1].trim()

        val roles = if (lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex + 1) else emptyList()

        //return Triple(user, pass, roles.toTypedArray())
        return UserTraits(user, pass, roles.toTypedArray())
    }

}