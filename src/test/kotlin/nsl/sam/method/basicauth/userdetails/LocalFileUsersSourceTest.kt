package nsl.sam.method.basicauth.userdetails

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.lang.RuntimeException

internal class LocalFileUsersSourceTest {

    @Test
    fun userWithNoRoles() {
        val usersSource = LocalFileUsersSource("src/test/config/passwords-no-roles.conf")
        assertEquals(1, usersSource.usersNumber())
        assertEquals(true, usersSource.isAvailable())
    }

    @Test
    fun testEmptyFile() {
        val usersSource = LocalFileUsersSource("src/test/config/passwords-empty.conf")
        assertEquals(0, usersSource.usersNumber())
        assertEquals(false, usersSource.isAvailable())
    }

    @Test
    fun testBlankFilePath() {
        val usersSource = LocalFileUsersSource("")
        assertEquals(0, usersSource.usersNumber())
        assertEquals(false, usersSource.isAvailable())
    }

    @Test
    fun testWrongFileFormat() {
        val path = "src/test/config/passwords-wrong-format.conf"

        val exception =assertThrows(RuntimeException::class.java) {
            LocalFileUsersSource(path)
        }

        assertEquals(
                String.format(LocalFileUsersImporter.WRONG_FORMAT_MESSAGE, path),
                exception.message
        )
    }

    @Test
    fun testWrongFilePath() {
        assertThrows(FileNotFoundException::class.java) {
            LocalFileUsersSource("not/existing/path")
        }
    }

    @Test
    fun testMixed() {
        val usersSource = LocalFileUsersSource("src/test/config/passwords-mixed.conf")
        assertEquals(8, usersSource.usersNumber())
        assertEquals(true, usersSource.isAvailable())
    }

}