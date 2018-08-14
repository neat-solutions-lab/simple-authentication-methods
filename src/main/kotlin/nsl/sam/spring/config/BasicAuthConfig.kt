package nsl.sam.spring.config

import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.method.basicauth.BasicAuthMethodRegistar
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import nsl.sam.sender.ResponseSender
import nsl.sam.spring.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

@Configuration
class BasicAuthConfig {

    @Autowired
    @Qualifier("unauthenticatedAccessResponseSender")
    lateinit var unauthenticatedResponseSender: ResponseSender

    @Bean
    fun usersSource(): UsersSource {
        return LocalFileUsersSource()
    }

    @Bean
    fun localUsersDetailsService(): UserDetailsService {
        return LocalUserDetailsService()
    }

    @Bean
    fun simpleAuthenticationEntryPointForHttpBasic(): AuthenticationEntryPoint {
        return SimpleFailedAuthenticationEntryPoint(unauthenticatedResponseSender)
    }

    @Bean
    fun basicAuthRegistar(
            @Qualifier("localUsersDetailsService")
            localUsersDetailsService: UserDetailsService,

            @Qualifier("simpleAuthenticationEntryPointForHttpBasic")
            simpleAuthenticationEntryPoint: AuthenticationEntryPoint)
            : AuthMethodRegistar {

        return BasicAuthMethodRegistar(localUsersDetailsService, simpleAuthenticationEntryPoint)
    }
}