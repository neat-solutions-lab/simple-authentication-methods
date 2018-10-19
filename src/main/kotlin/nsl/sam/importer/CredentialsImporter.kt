package nsl.sam.importer

import nsl.sam.changes.Changeable
import nsl.sam.changes.ChangeDetector
import nsl.sam.importer.parser.CredentialsParser
import nsl.sam.importer.reader.CredentialsReader
import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.interfaces.Resettable
import nsl.sam.logger.logger
import java.io.Closeable

open class CredentialsImporter<T>(
        private val credentialParser: CredentialsParser<T>,
        private val credentialsReader: CredentialsReader
) : Iterator<T>, ItemsAvailabilityAware, Closeable, Resettable, Changeable<String> {

    companion object {
        val log by logger()
    }

    private var changeDetector: ChangeDetector<String>? = null

    private var noMoreElementsInReader = false

    private var nextElement: T? = null

    override fun hasChangeDetector(): Boolean {
        return null != changeDetector
    }

    override fun getChangeDetector(): ChangeDetector<String>? {
        return changeDetector
    }

    override fun setChangeDetector(detector: ChangeDetector<String>) {
        this.changeDetector = detector
    }

    override fun hasNext(): Boolean {
        if(null != nextElement) return true
        nextElement = retrieveNextElement()
        return null != nextElement
    }

    override fun next(): T {
        if(null == nextElement) nextElement = retrieveNextElement()

        if(null == nextElement)
            throw NoSuchElementException("CredentialsImporter has not got any more elements.")

        val result = nextElement!!
        nextElement = null
        return result
    }

    private fun retrieveNextElement(): T? {

        if(noMoreElementsInReader) return null

        do {
            val line = credentialsReader.readCredentials()
            if(line == null) {
                noMoreElementsInReader = true
                return null
            }

            try {
                return credentialParser.parse(line)
            } catch (e: Exception) {
                log.error("Failed to parse credentials line: ${e.message}")
                continue
            }
        } while (true)
    }

    override fun reset() {
        noMoreElementsInReader = false
        nextElement = null
        credentialsReader.reset()
    }

    override fun close() {
        noMoreElementsInReader = false
        nextElement = null
        credentialsReader.close()
    }

    override fun hasItems(): Boolean {
        reset()
        val answer = hasNext()
        close()
        return answer
    }

    override fun toString(): String {
        return "${this::class.simpleName}(${credentialParser::class.simpleName}, ${credentialsReader::class.simpleName})"
    }

}
