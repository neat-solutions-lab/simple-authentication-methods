package nsl.sam.functional.entrypointfactory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [SpecificTestConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationMetadataResolverSpecificFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationMetadataResolverFindsFactorySpecifiedWithHelpOfSimpleBasicAuthenticationAnnotation() {

        val resolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata!!)
                .annotationTypes(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class)
                .build()

        val factoryClasses = resolver?.getAttributeValueAsArray(
                "authenticationEntryPointFactory", AuthenticationEntryPointFactory::class
        )
        val factoryClass = factoryClasses!![0]

        Assertions.assertThat(factoryClasses?.size).isEqualTo(1)
        Assertions.assertThat(factoryClass).isEqualTo(FirstTestTimeEntryPointFactory::class)
    }

}

class TestingSpecificImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationMetadataResolverSpecificFT.importingClassMetadata = importingClassMetadata
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(authenticationEntryPointFactory = [FirstTestTimeEntryPointFactory::class])
@Import(TestingSpecificImportBeanDefinitionRegistrar::class)
class SpecificTestConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
