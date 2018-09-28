package nsl.sam.spring.annotation

import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.Assert

/**
 * Represents attributes of [@EnableSimpleAuthentication] methods annotation
 */
data class EnableAnnotationAttributes private constructor (
        val annotationMetadata: AnnotationMetadata,
        val methods: Array<AuthenticationMethod>,
        val match: String,
        val debug: Boolean,
        val order: Int,
        val anonymousFallback: Boolean,
        val authorizations: String
) {

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor(){

        constructor(init: Builder.()->Unit):this() {
            init()
        }

        var annotationMetadata: AnnotationMetadata? = null
        var methods: Array<AuthenticationMethod> = emptyArray()
        var match = ""
        var debug = false
        var order = 0
        var anonymousFallback = false
        var authorizations = ""

        fun annotationMetadata(annotationMetadata: () -> AnnotationMetadata) = apply {this.annotationMetadata = annotationMetadata()}

        fun methods(methods: () -> Array<AuthenticationMethod>) = apply { this.methods = methods()}

        fun match(match: () -> String) = apply { this.match = match() }

        fun debug(debug: () -> Boolean) = apply { this.debug = debug() }

        fun order(order: () -> Int) = apply { this.order = order() }

        fun anonymousFallback(anonymousFallback: () -> Boolean) = apply { this.anonymousFallback = anonymousFallback() }

        fun authorizations(authorizations: () -> String) = apply { this.authorizations = authorizations() }

        fun build():EnableAnnotationAttributes {

            Assert.notNull(this.annotationMetadata, "annotationMetadata cannot be null")

            return EnableAnnotationAttributes(
                    this.annotationMetadata!!, this.methods, this.match, this.debug, this.order, this.anonymousFallback,
                    this.authorizations
            )
        }
    }
}