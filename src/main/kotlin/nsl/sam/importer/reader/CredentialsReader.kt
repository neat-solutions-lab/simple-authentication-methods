package nsl.sam.importer.reader

import nsl.sam.interfaces.Resettable
import java.io.Closeable

interface CredentialsReader: Closeable, Resettable {
    fun readCredentials():String?
}