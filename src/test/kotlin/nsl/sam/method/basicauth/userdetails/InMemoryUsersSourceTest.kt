package nsl.sam.method.basicauth.userdetails

import nsl.sam.method.basicauth.userdetails.importer.LocalFileUsersImporter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.lang.RuntimeException

internal class InMemoryUsersSourceTest {

    @Test
    fun userWithNoRoles() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-no-roles.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(1, usersSource.usersNumber())
        assertEquals(true, usersSource.isAvailable())
    }

    @Test
    fun testEmptyFile() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-empty.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(0, usersSource.usersNumber())
        assertEquals(false, usersSource.isAvailable())
    }

    @Test
    fun testBlankFilePath() {
        val localFileUsersImporter = LocalFileUsersImporter("")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(0, usersSource.usersNumber())
        assertEquals(false, usersSource.isAvailable())
    }

    @Test
    fun testWrongFileFormat() {
        val path = "src/test/config/passwords-wrong-format.conf"

        val localFileUsersImporter = LocalFileUsersImporter(path)

        val exception =assertThrows(RuntimeException::class.java) {
            InMemoryUsersSource(localFileUsersImporter)
        }

        assertEquals(
                String.format(LocalFileUsersImporter.WRONG_FORMAT_MESSAGE, path),
                exception.message
        )
    }

    @Test
    @Disabled("LocalFileUsersImporter has been refactored so that now it silently doesn't return users when source file cannot be found")
    fun testWrongFilePath() {

        val localFileUsersImporter = LocalFileUsersImporter("not/existing/path")
        assertThrows(FileNotFoundException::class.java) {
            InMemoryUsersSource(localFileUsersImporter)
        }
    }

    @Test
    fun testMixed() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-mixed.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(8, usersSource.usersNumber())
        assertEquals(true, usersSource.isAvailable())
    }

}