package nsl.sam.config

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity

abstract class SimpleAuthConfigurerAdapter: SimpleAuthConfigurer {
    override fun configure(http: HttpSecurity) {
    }

    override fun configure(authBuilder: AuthenticationManagerBuilder) {
    }
}