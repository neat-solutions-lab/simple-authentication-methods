package nsl.sam.annotation

import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class AnnotationMetadataResolver private constructor(
        private val annotationMetadata: AnnotationMetadata,
        private val annotationType: KClass<out Annotation>,
        private val parent: AnnotationMetadataResolver? = null) {

    fun isAnnotationPresent(): Boolean {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, true
                )
        )
        if (null == attributes && null != parent) {
            return parent.isAnnotationPresent()
        }
        return null != attributes
    }

    fun <T : Any> getAttributeValue(name: String, type: KClass<T>): T? {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, true
                )
        )

        val attributeValue = attributes?.get(name) ?: parent?.getAttributeValue(name, type)

        return type.cast(attributeValue)
    }

    fun <T : Any> getRequiredAttributeValue(name: String, type: KClass<T>): T {
        return getAttributeValue(name, type)!!
    }

    fun <T : Any> getAttributeValueAsArray(name: String, type: KClass<T>): Array<KClass<T>>? {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, false
                )
        )

        @Suppress("UNCHECKED_CAST")
        val attributeValue: Array<Class<T>>? = attributes?.get(name) as Array<Class<T>>?
        if (attributeValue == null || attributeValue.isEmpty()) {
            if (null != parent) return parent.getAttributeValueAsArray(name, type)
        }

        return attributeValue?.map { it.kotlin }?.toTypedArray()
    }

    fun <T : Any> getRequiredAttributeValueAsArray(name: String, type: KClass<T>): Array<KClass<T>> {
        return getAttributeValueAsArray(name, type)!!
    }

    @Suppress("UNUSED_PARAMETER", "UNCHECKED_CAST")
    fun <T : KClass<*>> getRequiredAttributeAsKClassArray(name: String, type: T): Array<T> {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, false
                )
        )

        val javaClasses = attributes?.get(name) as Array<Class<T>>?
        val kotlinClasses = javaClasses?.map { it.kotlin }?.toTypedArray() ?: emptyArray()
        return kotlinClasses as Array<T>
    }

    class Builder {
        private var annotationMetadata: AnnotationMetadata? = null
        private var annotationTypes: Array<out KClass<out Annotation>>? = null

        fun annotationMetadata(value: AnnotationMetadata) = apply { annotationMetadata = value }

        fun annotationTypes(vararg value: KClass<out Annotation>) = apply { annotationTypes = value }

        fun build(): AnnotationMetadataResolver {

            var beingBuildObject: AnnotationMetadataResolver? = null

            if (null == annotationTypes) {
                throw IllegalStateException(
                        "annotationTypes() has to be called before build()"
                )
            }

            if (null == annotationMetadata) {
                throw IllegalStateException(
                        "annotationMetadata() has to be called before build()"
                )
            }

            for (annotationType in annotationTypes!!) {
                beingBuildObject = AnnotationMetadataResolver(annotationMetadata!!, annotationType, beingBuildObject)
            }

            return beingBuildObject!!
        }
    }
}