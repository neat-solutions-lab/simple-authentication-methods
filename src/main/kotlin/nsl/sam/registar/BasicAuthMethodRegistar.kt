package nsl.sam.registar

import nsl.sam.logger.logger
import nsl.sam.userdetails.LocalFileUsersImporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService

@Order(10)
class BasicAuthMethodRegistar(
        @Qualifier("localUsersDetailsService") val localUsersDetailsService: UserDetailsService ) : AuthMethodRegistar {

    companion object { val log by logger() }

    @Value("\${sam.passwords-file:}")
    lateinit var passwordsFile: String

    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String

    @Autowired
    fun configureGlobal(authBuilder: AuthenticationManagerBuilder) {
        authBuilder.userDetailsService(localUsersDetailsService)
    }

    override fun isActive(): Boolean {
        if(passwordsFile.isNotBlank()) {
            if(serverAddress in arrayOf("localhost", "127.0.0.1") && usersNumber() == 0L) return false
            return true
        }
        return false
    }


    override fun register(http: HttpSecurity): HttpSecurity {
        log.info("Enabling Basic Http Auth method.")
        return http.httpBasic().and()

    }

    override fun methodName(): String {
        return "Local Basic Auth Method"
    }

    private fun usersNumber() : Long {

        val localUsersImporter = LocalFileUsersImporter(passwordsFile)
        var usersCounter = 0L
        while (localUsersImporter.hasNext()) {
            usersCounter++
            localUsersImporter.next()
        }

        return usersCounter
    }

}