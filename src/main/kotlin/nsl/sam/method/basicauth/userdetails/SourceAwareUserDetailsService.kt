package nsl.sam.method.basicauth.userdetails

import org.springframework.security.core.userdetails.UserDetailsService

interface SourceAwareUserDetailsService: UserDetailsService {

    /**
     * Checks if underlying [UsersSource] is able to provide at one one valid user
     * @see [UsersSource.isAvailable]
     */
    fun isUsersSourceAvailable(): Boolean

    fun usersNumber(): Int
}