package nsl.sam.method.basicauth.userdetails

import nsl.sam.method.basicauth.userdetails.impl.DefaultUserDetailsService
import nsl.sam.method.basicauth.usersimporter.impl.LocalFileUsersImporter
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.junit.jupiter.api.Test

internal class DefaultUserDetailsServiceTest {

    @Test
    fun userWithNoRolesTest() {

        val localFileUsersImporter = LocalFileUsersImporter("src/test/config/passwords-no-roles.conf")

        val userDetailsService = DefaultUserDetailsService(InMemoryUsersSource(localFileUsersImporter))
        val userDetails = userDetailsService.loadUserByUsername("test1")
        println("userDetails: $userDetails")

    }

}