package nsl.sam.method.basicauth.userdetails.importer

import nsl.sam.logger.logger
import java.io.File
import java.util.Scanner

class LocalFileUsersImporter(val path:String): UsersImporter {

    companion object {
        const val WRONG_FORMAT_MESSAGE = "Wrong format of the passwords file (%s)."
        private val log by logger()
    }

    private var currentLine: String? = null

    private val scanner : Scanner by lazy {
        Scanner(File(path))
    }

    override fun close() {
       scanner.close()
    }

    override fun hasNext(): Boolean {

        var nextLine: String? = null

        while(scanner.hasNextLine()) {
            nextLine = scanner.nextLine()
            log.debug("Line read from users file: ${nextLine.takeIf { it.length > 2 }?.substring(0, 2)}...trunced...")
            if(nextLine.trim().startsWith("#") || nextLine.isBlank()) continue
            break
        }

        if(nextLine != null && nextLine.isNotBlank() && !nextLine.startsWith("#")) {
            currentLine = nextLine
            return true
        }

        scanner.close()
        currentLine = null
        return false
    }

    override fun next(): Triple<String, String, Array<String>> {

        log.debug("next(); currentLine: ${currentLine?.takeIf { it.length > 2 }?.substring(0,2)}..")

        if (currentLine == null) throw IllegalStateException("next() method used on null currentLine")

        val lineParts: List<String> = currentLine!!.trim().split(Regex("\\s+"))
        if (lineParts.size < 1) throw RuntimeException("Wrong format of passwords file.")

        val userAndPassword : List<String> = lineParts[0].split(":")
        if (userAndPassword.size != 2) throw RuntimeException(String.format(WRONG_FORMAT_MESSAGE, path))

        val user = userAndPassword[0].trim()
        val pass = userAndPassword[1].trim()

        //val roles = if(lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex+1) else listOf("USER")
        val roles = if(lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex+1) else emptyList()

        return Triple(user, pass, roles.toTypedArray())
    }
}