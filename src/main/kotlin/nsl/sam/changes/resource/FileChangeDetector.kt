package nsl.sam.changes.resource

import nsl.sam.changes.AbstractChangeDetector
import nsl.sam.logger.logger
import java.io.File

class FileChangeDetector(private val path: String) : AbstractChangeDetector<String>() {

    companion object {
        val log by logger()
    }

    private var lastCheckFileExists: Boolean
    private var lastCheckLastModified = 0L
    private var lastCheckSize = 0L

    init {
        val file = File(path)
        lastCheckFileExists = file.exists()
        lastCheckLastModified = file.lastModified()
        lastCheckSize = file.length()
    }

    override fun getChangedResource(): String? {
        val file = File(path)
        val currentExists = file.exists()
        val currentLastModified = file.lastModified()
        val currentSize = file.length()

        //DebugLogBuffer.log("getChangedResource() [lastCheckFileExists: $lastCheckFileExists, lastCheckLastModified: $lastCheckLastModified]")
        //DebugLogBuffer.log("getChangedResource() [currentExists: $currentExists, currentLastModifies: $currentLastModified]")

        val changeDetected = hasChangeOccurred(currentExists, currentLastModified, currentSize)

        if (!changeDetected) return null

        lastCheckFileExists = currentExists
        lastCheckLastModified = currentLastModified
        lastCheckSize = currentSize

        return path
    }

    private fun hasChangeOccurred(currentExists: Boolean, currentLastModified: Long, currentSize: Long): Boolean {
        return if (currentExists)
            checkForChangeOfExistingFile(currentLastModified, currentSize)
        else
            checkForChangeOfNotExistingFile()
    }

    private fun checkForChangeOfNotExistingFile(): Boolean {
        return lastCheckFileExists
    }

    private fun checkForChangeOfExistingFile(currentLastModified: Long, currentSize: Long): Boolean {
        return (currentLastModified != lastCheckLastModified) || (currentSize != lastCheckSize)
    }
}
