package nsl.sam.config

import nsl.sam.logger.logger
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

const val USERNAME_NOT_FOUND_EXCEPTION_MESSAGE =
        "Fake UserDetailsService doesn't provide any users."


class DisableBasicAuthSimpleConfigurer: SimpleAuthConfigurerAdapter() {

    companion object { val log by logger() }

    override fun configure(http: HttpSecurity) {
        super.configure(http)
        log.info("Disabling HttpBasic Auth mechanism (due to @EnableSimpleAuthenticationMethods annotation parameters)")
        http?.httpBasic()?.disable()
    }

    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        super.configure(authBuilder)
        log.info("Configuring fake UserDetailsService (to suppress UserDetailsServiceAutoConfiguration in case of SpringBoot app)")
        authBuilder.userDetailsService(object : UserDetailsService {
            override fun loadUserByUsername(username: String?): UserDetails {
                throw UsernameNotFoundException(USERNAME_NOT_FOUND_EXCEPTION_MESSAGE)
            }
        })

    }
}