package nsl.sam.core.annotation

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.config.ordering.OrderingHelper
import org.springframework.core.type.AnnotationMetadata

object EnableAnnotationAttributesExtractor {

    fun extractAttributes(importingClassMetadata: AnnotationMetadata): EnableAnnotationAttributes {
        val annotationResolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata)
                .annotationTypes(EnableSimpleAuthenticationMethods::class)
                .build()

        return EnableAnnotationAttributes.Builder()
                .enableAnnotationMetadata(importingClassMetadata)
                .methods(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_METHODS_ATTRIBUTE_NAME, Array<AuthenticationMethod>::class
                ))
                .match(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_MATCH_ATTRIBUTE_NAME, String::class
                ))
                .debug(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_DEBUG_ATTRIBUTE_NAME, Boolean::class
                ))
                .order(calculateOrderValue(annotationResolver))
                .anonymousFallback(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME, Boolean::class
                ))
                .authorizations(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_AUTHORIZATIONS_ATTRIBUTE_NAME, String::class
                ))
                .forceHttps(annotationResolver.getRequiredAttributeValue(
                        ENABLE_ANNOTATION_FORCE_HTTPS_ATTRIBUTE_NAME, Boolean::class
                ))
                .build()
    }

    private fun calculateOrderValue(annotationResolver: AnnotationMetadataResolver): Int {
        val value = annotationResolver.getRequiredAttributeValue(
                ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME,
                Int::class
        )

        return when (value == -1) {
            true -> OrderingHelper.getSingleton().getNextNumber()
            else -> value
        }
    }
}