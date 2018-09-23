package nsl.sam.method.basicauth.annotation

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties

data class SimpleBasicAuthenticationAttributes(
        val passwordsFilePathProperty: String = "",
        val passwordsFilePath: String = ""
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

        fun passwordsFilePathProperty(passwordsFilePathProperty: ()->String) =
                apply { this.passwordsFilePathProperty = passwordsFilePathProperty() }

        fun passwordsFilePath(passwordsFilePath: ()->String) =
                apply { this.passwordsFilePath = passwordsFilePath() }

        fun build() = SimpleBasicAuthenticationAttributes(this.passwordsFilePathProperty, this.passwordsFilePath)
    }

}