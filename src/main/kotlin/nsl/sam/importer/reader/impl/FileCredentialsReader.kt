package nsl.sam.importer.reader.impl

import nsl.sam.importer.reader.CredentialsReader
import nsl.sam.logger.logger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class FileCredentialsReader(val path: String) : CredentialsReader {

    companion object {
        val log by logger()
    }

    private var bufferedReader: BufferedReader? = null

    init {
        when {
            path.isBlank() ->
                log.info("File path provided to ${this::class.simpleName} is an empty string. Reader will silently provide zero credentials.")
            !File(path).exists() ->
                log.warn("File $path passed to ${this::class.simpleName} constructor does not exist. " +
                        "Reader will silently provide zero credentials.")
        }
    }

    override fun readCredentials(): String? {

        log.debug("readCredentials()")

        var line: String?

        do {
            line = bufferedReader?.readLine()
        } while (line != null && (line.trim().startsWith("#") || line.isBlank()))

        return line
    }

    override fun close() {
        bufferedReader?.close()
    }

    override fun reset() {
        if (!File(path).exists()) return
        close()
        bufferedReader = BufferedReader(FileReader(File(path)))
    }
}