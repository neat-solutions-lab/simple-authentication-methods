package nsl.sam.method.basicauth.userdetails

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FileUsersImporterTest {

    @Test
    fun oneUserWithNoAnyAdditionalLines() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-one-user.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun oneUserWithOneEmptyLineAtTop() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-one-user-with-empty-line-at-top.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun oneUserWithOneEmptyLineAtBottom() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-one-user-with-empty-line-at-bottom.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun emptyFileTest() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-empty.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(0, resultAccumulator.size)
    }

    @Test
    fun onlyCommentsAndBlankLinesFileTest() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-comments-and-blank-lines.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(0, resultAccumulator.size)
    }

    @Test
    fun mixedContentTest() {
        val importer = PasswordsCredentialsImporter(FileCredentialsReader("src/test/config/passwords-mixed.conf"))
        importer.reset()
        val resultAccumulator = mutableListOf<UserTraits>()
        for (userTraits in importer) {
            resultAccumulator.add(userTraits)
            println("User details read from file: user: " +
                    "${userTraits.name}, password: ${userTraits.password}, roles: ${userTraits.roles}")
        }
        assertEquals(8, resultAccumulator.size)
    }
}