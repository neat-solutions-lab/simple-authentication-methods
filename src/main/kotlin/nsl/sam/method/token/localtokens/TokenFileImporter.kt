package nsl.sam.method.token.localtokens

import nsl.sam.logger.logger
import nsl.sam.method.token.filter.UserAndRoles
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileReader

class TokenFileImporter(val path: String) : Closeable, Iterator<LocalToken> {

    companion object { val log by logger() }

    private var currentLine : String? = null

    private val file : File by lazy {
        log.debug("File initialization")
        File(this.path)
    }

    private val bufferedReader : BufferedReader by lazy {
        log.debug("BufferedReader initialization")
        BufferedReader(FileReader(this.file))
    }

    override  fun next() : LocalToken {

        log.debug("next(); currentLine: ${currentLine?.let { it.takeIf{it.length>2}?.substring(0, 2) }}")

        if (currentLine == null) throw IllegalStateException("next() method used on null currentLine")

        val lineParts: List<String>  = currentLine!!.trim().split(Regex("\\s+"))
        if (lineParts.size < 2) throw RuntimeException("Wrong format of tokens file.")

        log.debug("lineParts size: ${lineParts.size}")

        val tokenValue = lineParts[0]
        val userName = lineParts[1]

        //val roles = if (lineParts.size > 2) lineParts.subList(2, lineParts.lastIndex+1) else listOf("USER")
        val roles = if (lineParts.size > 2) lineParts.subList(2, lineParts.lastIndex+1) else emptyList()

        return LocalToken(tokenValue, UserAndRoles(userName, roles.map { "ROLE_${it}" }.toTypedArray()))
    }

    override fun hasNext(): Boolean {

        log.debug("hasNext()")

        var line : String?

        do {
            line = bufferedReader.readLine()
        } while(line != null && line.trim().startsWith("#"))

        if (line == null) {
            bufferedReader.close()
            this.currentLine = null
            return false
        }

        this.currentLine = line
        return true
    }


    override fun close() {
        bufferedReader.close()
    }

}