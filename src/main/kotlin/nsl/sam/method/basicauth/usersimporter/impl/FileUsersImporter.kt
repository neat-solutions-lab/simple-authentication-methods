package nsl.sam.method.basicauth.usersimporter.impl

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.parser.PasswordLineParser
import java.io.File
import java.util.*

class FileUsersImporter(val path: String) : UsersImporter {

    companion object {
        private val log by logger()
    }

    private var currentLine: String? = null

    private var scanner: Scanner? = null

    init {
        when {
            path.isNotBlank() ->
                log.info("File path provided to ${this::class.simpleName} is an empty string. Importer will silently provide zero users.")
            !File(path).exists() ->
                log.warn("File $path passed to ${this::class.simpleName} constructor does not exist. " +
                        "Importer silently will provide zero users.")
        }
    }

    override fun hasItems(): Boolean {
        if (!File(path).exists()) return false
        reset()
        val isItemAvailable = hasNext()
        reset()
        return isItemAvailable

    }

    override fun reset() {
        if (!File(path).exists()) return
        close()
        scanner = Scanner(File(path))
        currentLine = null
    }

    override fun close() {
        scanner?.close()
    }

    override fun hasNext(): Boolean {
        if (!File(path).exists()) return false

        var nextLine: String? = null

        while (scanner!!.hasNextLine()) {
            nextLine = scanner!!.nextLine()
            log.debug("Line read from users file: ${nextLine.takeIf { it.length > 2 }?.substring(0, 2)}...trunced...")
            if (nextLine.trim().startsWith("#") || nextLine.isBlank()) continue
            break
        }

        if (nextLine != null && nextLine.isNotBlank() && !nextLine.startsWith("#")) {
            currentLine = nextLine
            return true
        }

        scanner?.close()
        currentLine = null
        return false
    }

    override fun next(): Triple<String, String, Array<String>> {

        log.debug("next(); currentLine: ${currentLine?.takeIf { it.length > 2 }?.substring(0, 2)}..")

        if (currentLine == null) throw IllegalStateException("next() method used on null currentLine")

        return PasswordLineParser.parseToTriple(currentLine!!)

    }
}