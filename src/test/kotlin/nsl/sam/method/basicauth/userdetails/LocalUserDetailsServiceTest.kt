package nsl.sam.method.basicauth.userdetails

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LocalUserDetailsServiceTest {

    @Test
    fun userWithNoRolesTest() {

        val userDetailsService = LocalUserDetailsService(LocalFileUsersSource("src/test/config/passwords-no-roles.conf"))
        val userDetails = userDetailsService.loadUserByUsername("test1")
        println("userDetails: $userDetails")

    }

}