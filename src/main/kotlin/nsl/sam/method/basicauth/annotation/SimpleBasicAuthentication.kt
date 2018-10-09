package nsl.sam.method.basicauth.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class SimpleBasicAuthentication(
        val passwordsFilePropertyName: String = "",
        val passwordsFilePath: String = "",
        val users: Array<String> = [],
        val usersEnvPrefix: String = "",
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)