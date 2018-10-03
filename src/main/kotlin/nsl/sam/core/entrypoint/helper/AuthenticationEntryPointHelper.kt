package nsl.sam.core.entrypoint.helper

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactories
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.security.web.AuthenticationEntryPoint
import kotlin.reflect.KClass

object AuthenticationEntryPointHelper {

    fun getAuthenticationEntryPoint(
            environment: Environment,
            annotationMetadata: AnnotationMetadata,
            involvedAnnotationTypes: Array<KClass<out Annotation>>)
            : AuthenticationEntryPoint {

        var annotationMetadataResolver: AnnotationMetadataResolver? = null
        for(type in involvedAnnotationTypes) {
            annotationMetadataResolver = AnnotationMetadataResolver(annotationMetadata, type, annotationMetadataResolver)
        }

        val factory = AuthenticationEntryPointFactories.getFactory(
                annotationMetadataResolver!!, environment
        )

        return factory.create()
    }

}