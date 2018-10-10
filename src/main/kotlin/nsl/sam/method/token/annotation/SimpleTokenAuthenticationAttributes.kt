package nsl.sam.method.token.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

class SimpleTokenAuthenticationAttributes private constructor (
        val tokensFilePropertyName: String = "",
        val tokensFilePath: String = "",
        val tokens: Array<String> = arrayOf(),
        val tokensEnvPrefix: String = "",
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = arrayOf()
) {

    companion object {
        fun default() = SimpleTokenAuthenticationAttributes()
    }

    private constructor(builder: Builder):this(
            builder.tokensFilePropertyName,
            builder.tokensFilePath,
            builder.tokens,
            builder.tokensEnvPrefix,
            builder.authenticationEntryPointFactory
    )

    class Builder {
        var tokensFilePropertyName: String = ""
            private set

        var tokensFilePath: String = ""
            private set

        var tokens: Array<String> = arrayOf()

        var tokensEnvPrefix: String = ""

        var authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = emptyArray()
            private set

        fun tokensFilePropertyName(value: String) =
                apply { this.tokensFilePropertyName = value }

        fun tokensFilePath(value: String) =
                apply { this.tokensFilePath = value }

        fun tokens(value: Array<String>) =
                apply { this.tokens = value }

        fun tokensEnvPrefix(value: String) =
                apply { this.tokensEnvPrefix = value }

        fun authenticationEntryPointFactory(value: Array<KClass<out AuthenticationEntryPointFactory>>) =
                apply { this.authenticationEntryPointFactory = value }

        fun build() = SimpleTokenAuthenticationAttributes(this)

    }
}