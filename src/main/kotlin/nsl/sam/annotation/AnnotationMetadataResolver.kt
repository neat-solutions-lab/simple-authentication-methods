package nsl.sam.annotation

import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class AnnotationMetadataResolver(
        private val annotationMetadata: AnnotationMetadata,
        private val annotationType: KClass<*>,
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

    fun <T:Any> getAttributeValue(name: String, type: KClass<T>): T? {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, true
                )
        )

        val attributeValue = attributes?.get(name) ?: parent?.getAttributeValue(name, type)

        return type.cast(attributeValue)
    }


    fun <T:Any> getRequiredAttributeValue(name: String, type: KClass<T>): T {
        return getAttributeValue(name, type)!!
    }

    fun <T:Any> getAttributeValueAsArray(name: String, type: KClass<T>): Array<KClass<T>>? {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, false
                )
        )

        var attributeValue: Array<Class<T>>? = attributes?.get(name) as Array<Class<T>>?
        if (attributeValue == null || attributeValue.isEmpty()) {
            if(null != parent) return parent.getAttributeValueAsArray(name, type)
        }

        val kotlinClasses = attributeValue?.map {it.kotlin}?.toTypedArray()
        return kotlinClasses
    }

    fun <T:Any> getRequiredAttributeValueAsArray(name: String, type: KClass<T>): Array<KClass<T>> {
        return getAttributeValueAsArray(name, type)!!
    }

    @Suppress("UNUSED_PARAMETER")
    fun <T: KClass<*>> getRequiredAttributeAsKClassArray(name: String, type: T): Array<T> {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, false
                )
        )

        val javaClasses = attributes?.get(name) as Array<Class<T>>?
        val kotlinClasses = javaClasses?.map { it.kotlin }?.toTypedArray() ?: emptyArray()
        return kotlinClasses as Array<T>

    }
}