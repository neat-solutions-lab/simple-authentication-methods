package nsl.sam.method.basicauth.userdetails.impl

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.userdetails.AvailabilityAwareUserDetailsService
import nsl.sam.method.basicauth.userssource.UsersSource
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class DefaultUserDetailsService(private val usersSource: UsersSource) : AvailabilityAwareUserDetailsService {

    /**
     * Checks if underlying [UsersSource] is able to provide at once one valid user
     * @see [UsersSource.hasItems]
     */
    override fun hasItems(): Boolean {
        return usersSource.hasItems()
    }

    companion object {
        val log by logger()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        log.debug("Loading $username user")

        val userTraits = usersSource.getUserTraits(username)

        return User.builder()
                .username(username)
                .password(userTraits.password)
                .roles(*userTraits.roles)
                .build()
    }
}
