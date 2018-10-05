package nsl.sam.annotation.inject

import nsl.sam.annotation.AnnotationMetadataResolver
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.Assert
import kotlin.reflect.KClass

class InjectedObjectsProvider<T:Any> private constructor(
        private val attributeName: String,
        private val defaultFactoryPropertyName: String,
        private val involvedAnnotationTypes: List<KClass<out Annotation>>,
        private val annotationMetadata: AnnotationMetadata,
        private val environment: Environment,
        private val factoryType: KClass<out Factory<T>>,
        private val defaultFactory: KClass<out Factory<T>>? = null
) {

    fun getObjectFactory(): Factory<T> {
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

    fun getObject(): T {
        val factory = getObjectFactory()
        return factory.create()
    }

    class Builder<T:Any>(private val factoryType: KClass<out Factory<T>>) {
        private var attributeName: String = ""
        private var defaultFactoryPropertyName: String = ""
        private var involvedAnnotationTypes: List<KClass<out Annotation>> = emptyList()
        private var annotationMetadata: AnnotationMetadata? = null
        private var environment: Environment? = null
        private var defaultFactory: KClass<out Factory<T>>? = null

        fun attributeName(value: String) = apply { attributeName = value }
        fun defaultFactoryPropertyName(value: String) = apply { defaultFactoryPropertyName = value }
        fun involvedAnnotationTypes(value: List<KClass<out Annotation>>) = apply { involvedAnnotationTypes = value }
        fun annotationMetadata(value: AnnotationMetadata) = apply { annotationMetadata = value }
        fun environment(value: Environment) = apply { environment = value }
        fun defaultFactory(value: KClass<out Factory<T>>) = apply { defaultFactory = value }


        fun build(): InjectedObjectsProvider<T> {

            Assert.hasLength(attributeName, "attributeName has to be defined before build() is called")
            Assert.hasLength(defaultFactoryPropertyName, "defaultFactoryPropertyName has to be defined before build() is called")
            Assert.notEmpty(involvedAnnotationTypes, "involvedAnnotationTypes has to be defined before build() is called")
            Assert.notNull(annotationMetadata, "annotationMetadata has to be defined before build() is called")

            return InjectedObjectsProvider<T>(
                    attributeName,
                    defaultFactoryPropertyName,
                    involvedAnnotationTypes,
                    annotationMetadata!!,
                    environment!!,
                    factoryType,
                    defaultFactory
            )
        }
    }

}
