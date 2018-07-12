package nsl.sam.userdetails

import nsl.sam.logger.logger
import java.io.Closeable
import java.io.File
import java.util.*

class LocalFileUsersImporter(val path:String) : Closeable, Iterator<Triple<String, String, Array<String>>> {

    companion object { val log by logger() }

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
            log.debug("Line read from users file: ${nextLine.takeIf { it.length > 2 }?.substring(0, 2)}...")
            if(nextLine.trim().startsWith("#")) continue
            break
        }

        if(nextLine != null && !nextLine.startsWith("#")) {
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
        if (userAndPassword.size != 2) throw RuntimeException("Wrong format of passwords file.")

        val user = userAndPassword[0].trim()
        val pass = userAndPassword[1].trim()

        val roles = if(lineParts.size > 1) lineParts.subList(1, lineParts.lastIndex+1) else listOf("USER")

        return Triple(user, pass, roles.toTypedArray())
    }
}