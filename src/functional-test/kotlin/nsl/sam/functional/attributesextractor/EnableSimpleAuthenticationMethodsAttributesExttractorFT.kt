package nsl.sam.functional.attributesextractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import kotlin.reflect.KClass

class EnableSimpleAuthenticationMethodsAttributesExttractorFT {

    private val simpleMetadataReaderFactory = SimpleMetadataReaderFactory()

    private fun getAnnotationAttributes(clazz: KClass<*>): EnableAnnotationAttributes {
        val metadataReader = simpleMetadataReaderFactory.getMetadataReader(
                clazz.qualifiedName!!
        )
        val annotationMetadata = metadataReader.annotationMetadata
        return EnableAnnotationAttributesExtractor.extractAttributes(annotationMetadata)
    }

    @Test
    fun forceHttpsAttributeIsFalseByDefault() {
        val annotationAttributes = getAnnotationAttributes(AnnotatedClassWithDefaultForcedHttps::class)
        Assertions.assertThat(annotationAttributes.forceHttps).isEqualTo(false)
    }

    @Test
    fun forceHttpsAttributeIsFalseWhenSoSet() {
        val annotationAttributes = getAnnotationAttributes(AnnotatedClassWithDisabledForcedHttps::class)
        Assertions.assertThat(annotationAttributes.forceHttps).isEqualTo(false)
    }

    @Test
    fun forceHttpsAttributeIsTrueWhenSoSet() {
        val annotationAttributes = getAnnotationAttributes(AnnotatedClassWithEnabledForcedHttps::class)
        Assertions.assertThat(annotationAttributes.forceHttps).isEqualTo(true)
    }
}

@EnableSimpleAuthenticationMethods
class AnnotatedClassWithDefaultForcedHttps

@EnableSimpleAuthenticationMethods(forceHttps = false)
class AnnotatedClassWithDisabledForcedHttps

@EnableSimpleAuthenticationMethods(forceHttps = true)
class AnnotatedClassWithEnabledForcedHttps
