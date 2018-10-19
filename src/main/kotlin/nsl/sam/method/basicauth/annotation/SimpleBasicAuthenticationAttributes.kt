package nsl.sam.method.basicauth.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

class SimpleBasicAuthenticationAttributes private constructor(
        val passwordsFilePathProperty: String = "",
        val passwordsFilePath: String = "",
        val detectPasswordsFileChanges: Boolean = false,
        val users: Array<String> = arrayOf(),
        val usersEnvPrefix: String = "",
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
) {

    companion object {
        /**
         * In case no [SimpleBasicAuthentication] annotation is used explicitly,
         * the default values of attributes are used.
         */
        fun default() = SimpleBasicAuthenticationAttributes()
    }

    private constructor (builder: Builder) : this(
            builder.passwordFilePathProperty, builder.passwordFilePath, builder.detectPasswordsFileChanges,
            builder.users, builder.usersEnvPrefix, builder.authenticationEntryPointFactory
    )

    class Builder {
        var passwordFilePathProperty: String = ""
            private set

        var passwordFilePath: String = ""
            private set

        var detectPasswordsFileChanges: Boolean = false
            private set

        var users: Array<String> = arrayOf()

        var usersEnvPrefix: String = ""

        var authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
            private set

        fun passwordFilePathProperty(value: String) =
                apply { this.passwordFilePathProperty = value }

        fun passwordFilePath(value: String) =
                apply { this.passwordFilePath = value }

        fun detectPasswordsFileChanges(value: Boolean) =
                apply { this.detectPasswordsFileChanges = value }

        fun users(value: Array<String>) = apply { this.users = value }

        fun usersEnvPrefix(value: String) =
                apply { this.usersEnvPrefix = value }

        fun authenticationEntryPointFactory(value: Array<KClass<out AuthenticationEntryPointFactory>>) =
                apply { this.authenticationEntryPointFactory = value }

        fun build() = SimpleBasicAuthenticationAttributes(this)
    }
}