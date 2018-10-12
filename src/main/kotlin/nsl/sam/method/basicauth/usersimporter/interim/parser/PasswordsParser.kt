package nsl.sam.method.basicauth.usersimporter.interim.parser

import nsl.sam.importer.CredentialsParser
import nsl.sam.importer.CredentialsParsingException

class PasswordsParser : CredentialsParser<Triple<String, String, Array<String>>>  {

    companion object {
        const val SYNTAX_ERROR_MESSAGE =
                "Could not parse passed string line into Triple with user, password and roles."
    }

    override fun parse(line: String): Triple<String, String, Array<String>> {

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

        return Triple(user, pass, roles.toTypedArray())
    }

}