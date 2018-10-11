package nsl.sam.functional.usersimporter

import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.impl.FileUsersImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OnlyCommentsFileUsersImporterFT {

    lateinit var usersImporter: UsersImporter

    @BeforeEach
    fun createFileUsersImporter() {
        usersImporter = FileUsersImporter("src/functional-test/config/only-comments-passwords.conf")
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