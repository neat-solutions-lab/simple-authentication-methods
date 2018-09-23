package nsl.sam.method.basicauth.userdetails

import org.springframework.security.core.userdetails.UserDetailsService

interface SourceAwareUserDetailsService: UserDetailsService {
    fun isUsersSourceAvailable(): Boolean
    fun usersNumber(): Int
}