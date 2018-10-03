package nsl.sam.method.basicauth.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

class SimpleBasicAuthenticationAttributes private constructor(
        val passwordsFilePathProperty: String = "",
        val passwordsFilePath: String = "",
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
) {

    companion object {
        fun default() = SimpleBasicAuthenticationAttributes()
    }

    private constructor (builder: Builder):this(
            builder.passwordFilePathProperty, builder.passwordFilePath, builder.authenticationEntryPointFactory
    )

    class Builder {
        var passwordFilePathProperty: String = ""
            private set

        var passwordFilePath: String = ""
            private set

        var authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
            private set

        fun passwordFilePathProperty(passwordFilePathProperty: String) =
                apply { this.passwordFilePathProperty = passwordFilePathProperty }

        fun passwordFilePath(passwordFilePath: String) =
                apply { this.passwordFilePath = passwordFilePath }

        fun authenticationEntryPointFactory(authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>>) =
                apply { this.authenticationEntryPointFactory = authenticationEntryPointFactory }

        fun build() = SimpleBasicAuthenticationAttributes(this)
    }
}