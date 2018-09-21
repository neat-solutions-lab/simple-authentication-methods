package nsl.sam.annotation

import kotlin.reflect.KClass

data class AnnotationAttributeDefinition<T: Annotation>(
        val annotationClass: KClass<T>,
        val attrName: String,
        val attrValue: Any
)