package nsl.sam.annotation.inject

import nsl.sam.annotation.AnnotationMetadataResolver
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass

object InjectedObjectsProvider {

    fun <T:Any> getObjectFactory(
            attributeName: String,
            defaultFactoryPropertyName: String,
            involvedAnnotationTypes: List<KClass<out Annotation>>,
            annotationMetadata: AnnotationMetadata,
            environment: Environment,
            factoryType: KClass<out Factory<T>>,
            defaultFactory: KClass<out Factory<T>>? = null
    ): Factory<T> {
        var annotationMetadataResolver: AnnotationMetadataResolver? = null
        for(type in involvedAnnotationTypes) {
            annotationMetadataResolver = AnnotationMetadataResolver(annotationMetadata, type, annotationMetadataResolver)
        }

        return FactoryRetriever.getFactory(
                factoryType,
                attributeName,
                annotationMetadataResolver!!,
                environment,
                defaultFactoryPropertyName,
                defaultFactory
        )
    }

    fun <T:Any> getObject(
            attributeName: String,
            defaultFactoryPropertyName: String,
            involvedAnnotationTypes: List<KClass<out Annotation>>,
            annotationMetadata: AnnotationMetadata,
            environment: Environment,
            factoryType: KClass<out Factory<T>>,
            defaultFactory: KClass<out Factory<T>>? = null
    ): T {
        val factory = getObjectFactory(
                attributeName,
                defaultFactoryPropertyName,
                involvedAnnotationTypes,
                annotationMetadata,
                environment,
                factoryType,
                defaultFactory
        )
        return factory.create()
    }
}
