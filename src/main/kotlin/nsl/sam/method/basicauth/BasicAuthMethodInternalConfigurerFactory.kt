package nsl.sam.method.basicauth

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import nsl.sam.spring.annotation.AuthenticationMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    //@Autowired
    //lateinit var localUsersDetailsService: UserDetailsService

    @Autowired
    lateinit var simpleAuthenticationEntryPoint: AuthenticationEntryPoint

    @Value("\${sam.passwords-file:}")
    lateinit var passwordsFile: String


    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String


    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_BASIC_AUTH
    }

    override fun create(attributes: Any): AuthMethodInternalConfigurer {


        /*
         * To determine:
         * - instance of UsersDetailsService (from Env)
         * - instance UsersSource
         * - configure UsersSource and pass it to UsersDetailsService
         */

        /*
         *
         */

        val usersSource = LocalFileUsersSource(passwordsFile)
        val usersDetailsService = LocalUserDetailsService(usersSource)

        return BasicAuthMethodInternalConfigurer(
                usersDetailsService,
                simpleAuthenticationEntryPoint,
                passwordsFile,
                serverAddress)



//        return BasicAuthMethodInternalConfigurer(
//                localUsersDetailsService,
//                simpleAuthenticationEntryPoint,
//                passwordsFile,
//                serverAddress)
    }
}