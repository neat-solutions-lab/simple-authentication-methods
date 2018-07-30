package nsl.sam.spring.config

import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.method.basicauth.BasicAuthMethodRegistar
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService

class BasicAuthConfig {

    @Bean
    fun usersSource(): UsersSource {
        return LocalFileUsersSource()
    }

    @Bean("localUsersDetailsService")
    fun localUsersDetailsService(): UserDetailsService {
        return LocalUserDetailsService()
    }

    @Bean
    fun basicAuthRegistar(
            @Qualifier("localUsersDetailsService")  localUsersDetailsService: UserDetailsService)
            : AuthMethodRegistar {
        return BasicAuthMethodRegistar(localUsersDetailsService)
    }

}