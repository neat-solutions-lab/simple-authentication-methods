package nsl.sam.annotation

import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class AnnotationMetadataResolver(
        private val annotationMetadata: AnnotationMetadata,
        private val annotationType: KClass<*>,
        private val parent: AnnotationMetadataResolver? = null) {


    fun <T:Any> getAttributeValue(name: String, type: KClass<T>): T? {

        val attributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        annotationType.qualifiedName!!, true
                )
        )

        val attributeValue = attributes?.get(name) ?: parent?.getAttributeValue(name, type)

        return type.cast(attributeValue)
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

}