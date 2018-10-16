package nsl.sam.functional.usersimporter
import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CorruptedPasswordsFileFileUsersImporterFT {

    @Test
    fun corruptedLinesOmittedWhenProcessingCorruptedPasswordsFile() {

        var usersImporter = PasswordsCredentialsImporter(FileCredentialsReader("src/functional-test/config/corrupted-passwords.conf"))

        var credentialsCounter = 0

        usersImporter.reset()
        usersImporter.use {
            for(user in it) {
                println("user: $user")
                credentialsCounter++
            }
        }
        Assertions.assertThat(credentialsCounter).isEqualTo(2)
    }

}
