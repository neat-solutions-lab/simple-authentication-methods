package nsl.sam.importer.reader

import nsl.sam.importer.CredentialsReader

open class ArrayCredentialsReader(private val credentialsArray: Array<String>) : CredentialsReader {

    private var currentIndex = 0

    override fun readCredentials(): String? {

        var readCredentials: String? = null

        while(currentIndex < credentialsArray.size) {
            val currentElement = credentialsArray[currentIndex++]
            if(currentElement.trim().startsWith("#")) continue
            readCredentials = currentElement
            break
        }

        return readCredentials
    }

    override fun close() {
        // NOTHING TO DO HERE
    }

    override fun reset() {
        currentIndex = 0
    }
}