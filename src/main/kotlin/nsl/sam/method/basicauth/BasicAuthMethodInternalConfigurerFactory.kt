package nsl.sam.method.basicauth

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.spring.annotation.AuthenticationMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    @Autowired
    lateinit var localUsersDetailsService: UserDetailsService

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

        return BasicAuthMethodInternalConfigurer(
                localUsersDetailsService,
                simpleAuthenticationEntryPoint,
                passwordsFile,
                serverAddress)
    }

}