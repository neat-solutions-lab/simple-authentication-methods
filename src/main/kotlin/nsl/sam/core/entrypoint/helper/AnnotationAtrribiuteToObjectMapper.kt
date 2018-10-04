package nsl.sam.core.entrypoint.helper

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.entrypoint.factory.FactoriesCaches
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object AnnotationAtrribiuteToObjectMapper {


//    fun <I:Any, T:I> getObject(
//            environment: Environment,
//            annotationMetadata: AnnotationMetadata,
//            involvedAnnotationTypes: Array<KClass<out Annotation>>,
//            interfaceType: KClass<I>,
//            actualObjectType: KClass<T>)
//            : T? {
//
//        var annotationMetadataResolver: AnnotationMetadataResolver? = null
//        for(type in involvedAnnotationTypes) {
//            annotationMetadataResolver = AnnotationMetadataResolver(annotationMetadata, type, annotationMetadataResolver)
//        }
//
//        val factories = FactoriesCaches.getFactoriesRegistry(interfaceType)
//
//        val factory = factories.getOrPut(actualObjectType) {
//            actualObjectType.createInstance()
//        }
//
//        factory.create()
//
//        return null
//
//    }

}