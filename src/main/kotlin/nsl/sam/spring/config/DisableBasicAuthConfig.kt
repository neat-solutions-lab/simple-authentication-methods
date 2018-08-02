package nsl.sam.spring.config

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class DisableBasicAuthConfig : WebSecurityConfigurerAdapter() {

    companion object { val log by logger() }

    override fun configure(http: HttpSecurity?) {
        super.configure(http)
        log.info("Disabling HttpBasic Auth mechanism (due to @EnableSimpleAuthenticationMethods annotation parameters)")
        http?.httpBasic()?.disable()
    }

    @Autowired
    fun configureGlobal(authBuilder: AuthenticationManagerBuilder) {

        log.info("Configuring fake UserDetailsService (to suppress UserDetailsServiceAutoConfiguration in case of SpringBoot app)")
        authBuilder.userDetailsService(object : UserDetailsService {
            override fun loadUserByUsername(username: String?): UserDetails {
                throw UsernameNotFoundException("Fake UserDetailsService doesn't provide any users.")
            }
        })
    }



}