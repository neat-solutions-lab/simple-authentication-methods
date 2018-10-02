package nsl.sam.method.basicauth.annotation

import nsl.sam.spring.entrypoint.AuthenticationEntryPointFactory
import nsl.sam.spring.entrypoint.NullAuthenticationEntryPointFactory
import kotlin.reflect.KClass

data class SimpleBasicAuthenticationAttributes(
        val passwordsFilePathProperty: String = "",
        val passwordsFilePath: String = "",
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
) {

    companion object {
        fun create(init: Builder.()->Unit) = Builder(init).build()
        fun default() = SimpleBasicAuthenticationAttributes()
    }

    class Builder private constructor() {

        constructor(init: Builder.()->Unit):this() {
            init()
        }

        var passwordsFilePathProperty = ""
        var passwordsFilePath = ""
        var authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()

        fun passwordsFilePathProperty(passwordsFilePathProperty: ()->String) =
                apply { this.passwordsFilePathProperty = passwordsFilePathProperty() }

        fun passwordsFilePath(passwordsFilePath: ()->String) =
                apply { this.passwordsFilePath = passwordsFilePath() }

        fun authenticationEntryPointFactory(authenticationEntryPointFactory: () -> Array<KClass<out AuthenticationEntryPointFactory>>) =
                apply {
                    this.authenticationEntryPointFactory = authenticationEntryPointFactory()}

        fun build() = SimpleBasicAuthenticationAttributes(
                this.passwordsFilePathProperty,
                this.passwordsFilePath,
                this.authenticationEntryPointFactory
        )
    }
}