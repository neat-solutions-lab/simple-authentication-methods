package nsl.sam.functional.userssource

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class DuplicateUsersInMemoryUsersSourceFT {

    @Test
    fun inCaseOfDuplicateUsersTheLastOneWins() {

        val fileCredentialsReader = FileCredentialsReader("src/functional-test/config/duplicate-passwords.conf")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )
        val passwordAndRoles = inMemoryUsersSource.getUserTraits("test")

        /*
         * ensure that the password belongs to the last user of name "test" listed in
         * the file with users
         */
        Assertions.assertThat(passwordAndRoles.password).isEqualTo("{noop}double")
        println("passwordAndRoles: $passwordAndRoles")
    }

}