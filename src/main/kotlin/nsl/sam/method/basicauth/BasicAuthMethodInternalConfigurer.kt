package nsl.sam.method.basicauth

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.userdetails.LocalFileUsersImporter
import nsl.sam.registar.AuthMethodInternalConfigurer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurer(
        private val localUsersDetailsService: UserDetailsService,
        private val simpleAuthenticationEntryPoint: AuthenticationEntryPoint,
        private val passwordsFile: String,
        private val serverAddress: String) : AuthMethodInternalConfigurer {

    companion object { val log by logger() }


    private var isActiveVariableAlreadyCalculated = false
    private var isActiveValue = false


    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        if(!isActive()) return
        authBuilder.userDetailsService(localUsersDetailsService)
    }

    private fun isActiveInternal(): Boolean {
        if (passwordsFile.isBlank()) return false

        if(serverAddress in arrayOf("localhost", "127.0.0.1") && usersNumber() == 0L) {
            return false
        }

        return true
    }

    override fun isActive(): Boolean {
        if (!isActiveVariableAlreadyCalculated) {
            isActiveValue = isActiveInternal()
            isActiveVariableAlreadyCalculated = true

        }
        return isActiveValue
    }

    override fun register(http: HttpSecurity): HttpSecurity {
        log.info("Enabling HttpBasic Auth method.")
        return http.httpBasic().authenticationEntryPoint(simpleAuthenticationEntryPoint).and()
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