package nsl.sam.method.basicauth.userdetails

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class DefaultUserDetailsService(private val usersSource: UsersSource) : SourceAwareUserDetailsService {

    override fun usersNumber(): Int {
        return usersSource.usersNumber()
    }

    override fun isUsersSourceAvailable(): Boolean {
        return usersSource.isAvailable()
    }

    companion object { val log by logger() }

    override fun loadUserByUsername(username: String): UserDetails {
        log.debug("Loading $username user")

        val (pass, roles) = usersSource.getUserPasswordAndRoles(username)
        log.debug("Building User object for $username user")
        return User.builder()
                .username(username)
                .password(pass)
                .roles(*roles)
                .build()
    }
}