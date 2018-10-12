package nsl.sam.method.basicauth.usersimporter.parser

object PasswordLineParser {

    const val SYNTAX_ERROR_MESSAGE = "User defining line syntax error. Could not parse passed string line into Triple with user, password and roles."

    fun parseToTriple(line: String): Triple<String, String, Array<String>> {

        if (line.isBlank())
            throw PasswordLineParsingException(SYNTAX_ERROR_MESSAGE)

        val lineParts: List<String> = line.trim().split(Regex("\\s+"))
        if (lineParts.isEmpty())
            throw PasswordLineParsingException(SYNTAX_ERROR_MESSAGE)

        val userAndPassword: List<String> = lineParts[0].split(":")
        if (userAndPassword.size != 2)
            throw PasswordLineParsingException(SYNTAX_ERROR_MESSAGE)

        val user = userAndPassword[0].trim()
        val pass = userAndPassword[1].trim()

        val roles = if (lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex + 1) else emptyList()

        return Triple(user, pass, roles.toTypedArray())
    }

}