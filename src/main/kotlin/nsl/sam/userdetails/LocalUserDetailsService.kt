package nsl.sam.userdetails

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

class LocalUserDetailsService : UserDetailsService {

    companion object { val log by logger() }

    @Autowired
    lateinit var usersSource: UsersSource

    override fun loadUserByUsername(username: String): UserDetails {
        log.debug("Loading ${username} user")

        val (pass, roles) = usersSource.getUserPasswordAndRoles(username)
        log.debug("Building User object for ${username} user")
        return User.builder()
                .username(username)
                .password(pass)
                .roles(*roles)
                .build()
    }
}