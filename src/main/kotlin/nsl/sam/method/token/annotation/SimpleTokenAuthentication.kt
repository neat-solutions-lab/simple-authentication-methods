package nsl.sam.method.token.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class SimpleTokenAuthentication (
    val tokensFilePropertyName: String = "",
    val tokensFilePath: String = "",
    val tokens: Array<String> = [],
    val tokensEnvPrefix: String = "",
    val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)