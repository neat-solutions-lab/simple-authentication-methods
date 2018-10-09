package nsl.sam.method.basicauth.userdetails

import nsl.sam.method.basicauth.usersimporter.impl.LocalFileUsersImporter
import nsl.sam.method.basicauth.usersimporter.parser.BasicUserLineParser
import nsl.sam.method.basicauth.usersimporter.parser.BasicUserLineParsingException
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException

internal class InMemoryUsersSourceTest {

    @Test
    fun userWithNoRoles() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-no-roles.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(true, usersSource.hasItems())
    }

    @Test
    fun testEmptyFile() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-empty.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(false, usersSource.hasItems())
    }

    @Test
    fun testBlankFilePath() {
        val localFileUsersImporter = LocalFileUsersImporter("")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)
        assertEquals(false, usersSource.hasItems())
    }

    @Test
    fun testWrongFileFormat() {
        val path = "src/test/config/passwords-wrong-format.conf"

        val localFileUsersImporter = LocalFileUsersImporter(path)

        val exception = assertThrows(BasicUserLineParsingException::class.java) {
            InMemoryUsersSource(localFileUsersImporter)
        }

        assertEquals(
                String.format(BasicUserLineParser.SYNTAX_ERROR_MESSAGE, path),
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
        //assertEquals(8, usersSource.usersNumber())
        assertEquals(true, usersSource.hasItems())
    }

    @Test
    fun testIndividualUsersAvailability() {
        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-mixed.conf")
        val usersSource = InMemoryUsersSource(localFileUsersImporter)

        val users = arrayOf("test1", "test2", "test3", "test5", "test6", "test8", "test9", "test10")
        val passwordAndRolesList = mutableListOf<Pair<String, Array<String>>>()

        users.forEach {
            passwordAndRolesList.add(usersSource.getUserPasswordAndRoles(it))
        }

        Assertions.assertThat(passwordAndRolesList.size).isEqualTo(8)
        Assertions.assertThat(passwordAndRolesList).allMatch { it.first == "{noop}test" }
        Assertions.assertThat(passwordAndRolesList.stream().map { it.second })
                .allMatch { it.contains("USER") && it.contains("ADMIN") }
    }

}