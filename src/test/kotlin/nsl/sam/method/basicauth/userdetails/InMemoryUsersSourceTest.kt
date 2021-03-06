package nsl.sam.method.basicauth.userdetails

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class InMemoryUsersSourceTest {

    @Test
    fun userWithNoRoles() {
        val fileCredentialsReader = FileCredentialsReader("src/test/config/passwords-no-roles.conf")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )


        assertEquals(true, inMemoryUsersSource.hasItems())
    }

    @Test
    fun testEmptyFile() {
        val fileCredentialsReader = FileCredentialsReader("src/test/config/passwords-empty.conf")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )
        assertEquals(false, inMemoryUsersSource.hasItems())
    }

    @Test
    fun testBlankFilePath() {

        val fileCredentialsReader = FileCredentialsReader("")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )
        assertEquals(false, inMemoryUsersSource.hasItems())
    }


    @Test
    fun testMixed() {

        val fileCredentialsReader = FileCredentialsReader("src/test/config/passwords-mixed.conf")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )

        assertEquals(true, inMemoryUsersSource.hasItems())
    }

    @Test
    fun testIndividualUsersAvailability() {

        val fileCredentialsReader = FileCredentialsReader("src/test/config/passwords-mixed.conf")
        val inMemoryUsersSource = InMemoryUsersSource.createInstance(
                PasswordsCredentialsImporter(fileCredentialsReader),
                Properties()
        )

        val users = arrayOf("test1", "test2", "test3", "test5", "test6", "test8", "test9", "test10")
        val passwordAndRolesList = mutableListOf<UserTraits>()

        users.forEach {
            passwordAndRolesList.add(inMemoryUsersSource.getUserTraits(it))
        }

        Assertions.assertThat(passwordAndRolesList.size).isEqualTo(8)
        Assertions.assertThat(passwordAndRolesList).allMatch { it.password == "{noop}test" }
        Assertions.assertThat(passwordAndRolesList.stream().map { it.roles })
                .allMatch { it.contains("USER") && it.contains("ADMIN") }
    }
}