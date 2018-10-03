package nsl.sam.core.annotation

import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.Assert

/**
 * Represents attributes of [@EnableSimpleAuthentication] methods annotation
 */
class EnableAnnotationAttributes private constructor (
        val enableAnnotationMetadata: AnnotationMetadata,
        val methods: Array<AuthenticationMethod>,
        val match: String,
        val debug: Boolean,
        val order: Int,
        val anonymousFallback: Boolean,
        val authorizations: String
) {

    class Builder {
        var enableAnnotationMetadata: AnnotationMetadata? = null
        var methods: Array<AuthenticationMethod> = emptyArray()
        var match = ""
        var debug = false
        var order = 0
        var anonymousFallback = false
        var authorizations = ""

        fun enableAnnotationMetadata(enableAnnotationMetadata: AnnotationMetadata) =
                apply {this.enableAnnotationMetadata = enableAnnotationMetadata}

        fun methods(methods: Array<AuthenticationMethod>) =
                apply { this.methods = methods}

        fun match(match: String) =
                apply { this.match = match }

        fun debug(debug: Boolean) =
                apply { this.debug = debug }

        fun order(order: Int) =
                apply { this.order = order }

        fun anonymousFallback(anonymousFallback: Boolean) =
                apply { this.anonymousFallback = anonymousFallback }

        fun authorizations(authorizations: String) =
                apply { this.authorizations = authorizations }

        fun build():EnableAnnotationAttributes {
            Assert.notNull(this.enableAnnotationMetadata, "enableAnnotationMetadata cannot be null")
            return EnableAnnotationAttributes(
                this.enableAnnotationMetadata!!,
                this.methods, this.match, this.debug, this.order, this.anonymousFallback,
                this.authorizations)
        }
    }
}