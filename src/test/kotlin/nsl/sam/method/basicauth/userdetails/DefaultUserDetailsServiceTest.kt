package nsl.sam.method.basicauth.userdetails

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.userdetails.impl.DefaultUserDetailsService
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.interim.impl.InMemoryUsersSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class DefaultUserDetailsServiceTest {

    @Test
    fun userWithNoRolesTest() {

        val fileCredentialsReader = FileCredentialsReader("src/test/config/passwords-no-roles.conf")
        val inMemoryUsersSource = InMemoryUsersSource(PasswordsCredentialsImporter(fileCredentialsReader))
        val userDetailsService = DefaultUserDetailsService(inMemoryUsersSource)
        val userDetails = userDetailsService.loadUserByUsername("test1")

        Assertions.assertThat(userDetails.username).isEqualTo("test1")
        Assertions.assertThat(userDetails.authorities).isEmpty()
        println("userDetails: $userDetails")

    }

}