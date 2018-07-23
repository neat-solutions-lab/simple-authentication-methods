package nsl.sam.configurator

import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.registar.BasicAuthMethodRegistar
import nsl.sam.userdetails.LocalFileUsersSource
import nsl.sam.userdetails.LocalUserDetailsService
import nsl.sam.userdetails.UsersSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService

class BasicAuthConfigurator {

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