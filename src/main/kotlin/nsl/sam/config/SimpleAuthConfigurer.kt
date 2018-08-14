package nsl.sam.config

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity

interface SimpleAuthConfigurer {

    fun configure(http: HttpSecurity)
    fun configure(authBuilder: AuthenticationManagerBuilder)

}