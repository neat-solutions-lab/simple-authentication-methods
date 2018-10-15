package nsl.sam.functional.usersimporter

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OnlyCommentsFileUsersImporterFT {

    lateinit var usersImporter: PasswordsCredentialsImporter

    @BeforeEach
    fun createFileUsersImporter() {
        usersImporter = PasswordsCredentialsImporter(FileCredentialsReader("src/functional-test/config/only-comments-passwords.conf"))
    }

    @Test
    fun zeroUsersImportedFromFileUsersImporterWhenOnlyCommentsPasswordsFileUsed() {

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
    fun hasItemsReturnsFalseWhenOnlyCommentsPasswordsFileUsed() {
        Assertions.assertThat(usersImporter.hasItems()).isEqualTo(false)
    }
}