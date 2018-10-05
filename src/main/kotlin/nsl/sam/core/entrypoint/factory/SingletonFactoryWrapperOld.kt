package nsl.sam.core.entrypoint.factory

import org.springframework.security.web.AuthenticationEntryPoint

class SingletonFactoryWrapperOld(
        private val wrappedFactory: AuthenticationEntryPointFactory)
    : AuthenticationEntryPointFactory {

    @Volatile
    var authenticationEntryPointInstance: AuthenticationEntryPoint? = null


    @Synchronized
    override fun create(): AuthenticationEntryPoint {

        if (null == authenticationEntryPointInstance) {
            authenticationEntryPointInstance = wrappedFactory.create()
        }
        return authenticationEntryPointInstance!!
    }

}