package nsl.sam.functional.usersimporter

import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.impl.FileUsersImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DuplicateFileUsersImporterFT {

    lateinit var usersImporter: UsersImporter

    @BeforeEach
    fun createFileUsersImporter() {
        usersImporter = FileUsersImporter("src/functional-test/config/duplicate-passwords.conf")
    }

    @Test
    fun duplicateUsersAreJustPassedThrough() {

        var usersCounter = 0

        usersImporter.reset()
        usersImporter.use {
            for (user in usersImporter) {
                usersCounter++
            }
        }

        Assertions.assertThat(usersCounter).isEqualTo(4)
    }

    @Test
    fun hasItemsReturnsTrueWhenDuplicatesPasswordsFileUsed() {
        Assertions.assertThat(usersImporter.hasItems()).isEqualTo(true)
    }
}