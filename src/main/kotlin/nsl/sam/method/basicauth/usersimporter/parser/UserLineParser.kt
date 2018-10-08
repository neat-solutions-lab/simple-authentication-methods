package nsl.sam.method.basicauth.usersimporter.parser

import nsl.sam.method.basicauth.usersimporter.impl.LocalFileUsersImporter

object UserLineParser {

    const val SYNTAX_ERROR_MESSAGE = "User defining line syntax error. Could not parse passed string line into Triple with user, password and roles."

    fun parseToTriple(line: String):  Triple<String, String, Array<String>> {
        val lineParts: List<String> = line!!.trim().split(Regex("\\s+"))
        if (lineParts.size < 1) throw RuntimeException("Wrong format of passwords file.")

        val userAndPassword : List<String> = lineParts[0].split(":")
        if (userAndPassword.size != 2) throw
            RuntimeException(SYNTAX_ERROR_MESSAGE)

        val user = userAndPassword[0].trim()
        val pass = userAndPassword[1].trim()

        val roles = if(lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex+1) else emptyList()

        return Triple(user, pass, roles.toTypedArray())
    }

}