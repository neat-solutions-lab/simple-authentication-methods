package nsl.sam.method.basicauth.usersimporter.interim

import nsl.sam.importer.reader.FileCredentialsReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class PasswordsCredentialsImporterTest {

    @Test
    fun passwordsImporterGoesThroughPasswordsFileSuccessfully() {

       val passwordsCredentialsImporter = PasswordsCredentialsImporter(
               FileCredentialsReader("src/test/config/passwords.conf")
       )

        var usersCounter = 0

        passwordsCredentialsImporter.reset()
        passwordsCredentialsImporter.use {
            for(user in it) {
                println("user: $user")
                usersCounter++
            }
        }

        Assertions.assertThat(usersCounter).isEqualTo(3)
        Assertions.assertThat(passwordsCredentialsImporter.hasItems()).isEqualTo(true)
    }


    @Test
    fun hasItemsReturnsTrueWhenCommonPasswordsFileProcessed() {

        val passwordsCredentialsImporter = PasswordsCredentialsImporter(
                FileCredentialsReader("src/test/config/passwords.conf")
        )

        Assertions.assertThat(passwordsCredentialsImporter.hasItems()).isEqualTo(true)
    }

    @Test
    fun noErrorsWhenCorruptedPasswordsFileProcessed() {

        val passwordsCredentialsImporter = PasswordsCredentialsImporter(
                FileCredentialsReader("src/test/config/corrupted-passwords.conf")
        )

        var usersCounter = 0

        passwordsCredentialsImporter.reset()
        passwordsCredentialsImporter.use {
            for(user in it) {
                println("user: $user")
                usersCounter++
            }
        }

        Assertions.assertThat(usersCounter).isEqualTo(2)
        Assertions.assertThat(passwordsCredentialsImporter.hasItems()).isEqualTo(true)
    }

}