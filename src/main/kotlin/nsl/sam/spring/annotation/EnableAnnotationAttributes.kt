package nsl.sam.spring.annotation

/**
 * Represents attributes of [@EnableSimpleAuthentication] methods annotation
 */
data class EnableAnnotationAttributes private constructor (
        val className: String,
        val methods: Array<AuthenticationMethod>,
        val match: String,
        val debug: Boolean,
        val order: Int,
        val anonymousFallback: Boolean,
        val authentications: String,
        val deactivateNotConfigured: Boolean
) {

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor(){

        constructor(init: Builder.()->Unit):this() {
            init()
        }

        var className = ""
        var methods: Array<AuthenticationMethod> = emptyArray()
        var match = ""
        var debug = false
        var order = 0
        var anonymousFallback = false
        var authentications = ""
        var deactivateNotConfigured = false

        fun className(className: () -> String) = apply {this.className = className()}

        fun methods(methods: () -> Array<AuthenticationMethod>) = apply { this.methods = methods()}

        fun match(match: () -> String) = apply { this.match = match() }

        fun debug(debug: () -> Boolean) = apply { this.debug = debug() }

        fun order(order: () -> Int) = apply { this.order = order() }

        fun anonymousFallback(anonymousFallback: () -> Boolean) = apply { this.anonymousFallback = anonymousFallback() }

        fun authentications(authentications: () -> String) = apply { this.authentications = authentications() }

        fun deactivateNotConfigured(deactivateNotConfigured: () -> Boolean) = apply { this.deactivateNotConfigured = deactivateNotConfigured() }

        fun build() = EnableAnnotationAttributes(
                this.className, this.methods, this.match, this.debug, this.order, this.anonymousFallback,
                this.authentications, this.deactivateNotConfigured
        )
    }

}