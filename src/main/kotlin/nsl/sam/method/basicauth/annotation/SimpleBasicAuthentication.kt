package nsl.sam.method.basicauth.annotation

import nsl.sam.spring.entrypoint.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class SimpleBasicAuthentication (

    val passwordsFilePropertyName: String = "",

    val passwordsFilePath: String = "",

    val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)