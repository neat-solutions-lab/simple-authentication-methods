package nsl.sam.functional.usersimporter

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmptyFileFileUsersImporterFT {

    lateinit var usersImporter: PasswordsCredentialsImporter

    @BeforeEach
    fun createFileUsersImporter() {
        usersImporter = PasswordsCredentialsImporter(FileCredentialsReader("src/functional-test/config/empty-passwords.conf"))
    }

    @Test
    fun zeroUsersImportedFromFileUsersImporterWhenEmptyPasswordsFileUsed() {

        var usersCounter = 0

        usersImporter.reset()
        usersImporter.use {
            for (user in usersImporter) {
                usersCounter++
            }
        }

        Assertions.assertThat(usersCounter).isEqualTo(0)
    }

    @Test
    fun hasItemsReturnsFalseWhenEmptyPasswordsFileUsed() {
        Assertions.assertThat(usersImporter.hasItems()).isEqualTo(false)
    }

}