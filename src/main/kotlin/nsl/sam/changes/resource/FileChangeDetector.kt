package nsl.sam.changes.resource

import nsl.sam.changes.AbstractChangeDetector
import java.io.File

class FileChangeDetector(private val path: String) : AbstractChangeDetector<String>() {

    private var lastCheckfileExists: Boolean
    private var lastCheckLastModified = 0L

    init {
        val file = File(path)
        lastCheckfileExists = file.exists()
        lastCheckLastModified = file.lastModified()
    }

    override fun getChangedResource(): String? {
        val file = File(path)
        val currentExists = file.exists()
        val currentLastModified = file.lastModified()

        val changeDetected = hasChangeOccurred(currentExists, currentLastModified)

        if(!changeDetected) return null

        lastCheckfileExists = file.exists()
        lastCheckLastModified = file.lastModified()

        return path
    }

    private fun hasChangeOccurred(currentExists: Boolean, currentLastModified: Long): Boolean {
        return if(currentExists)
            checkForChangeOfExistingFile(currentLastModified)
        else
            checkForChangeOfNotExistingFile()
    }

    private fun checkForChangeOfNotExistingFile(): Boolean {
        return lastCheckfileExists
    }

    private fun checkForChangeOfExistingFile(currentLastModified: Long): Boolean {
        return currentLastModified != lastCheckLastModified
    }
}