package nsl.sam.functional.userssource

import nsl.sam.method.basicauth.usersimporter.impl.FileUsersImporter
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DuplicateUsersInMemoryUsersSourceFT {

    @Test
    fun inCaseOfDuplicateUsersTheLastOneWins() {

        val fileUsersImporter = FileUsersImporter("src/functional-test/config/duplicate-passwords.conf")
        val inMemoryUsersSource = InMemoryUsersSource(fileUsersImporter)

        val passwordAndRoles = inMemoryUsersSource.getUserPasswordAndRoles("test")

        /*
         * ensure that the password belongs to the last user of name "test" listed in
         * the file with users
         */
        Assertions.assertThat(passwordAndRoles.first).isEqualTo("{noop}double")
        println("passwordAndRoles: $passwordAndRoles")
    }

}