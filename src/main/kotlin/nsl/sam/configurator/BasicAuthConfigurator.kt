package nsl.sam.configurator

import nsl.sam.registrant.AuthMethodRegistrant
import nsl.sam.registrant.BasicAuthMethodRegistrant
import nsl.sam.userdetails.LocalFileUsersSource
import nsl.sam.userdetails.LocalUserDetailsService
import nsl.sam.userdetails.UsersSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
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
            : AuthMethodRegistrant {
        return BasicAuthMethodRegistrant(localUsersDetailsService)
    }

}