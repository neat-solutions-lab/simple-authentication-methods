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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [GeneralTestConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationMetadataResolverGeneralFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationMetadataResolverFindsFactorySpecifiedWithHelpOfEnableSimpleAuthenticationMethodsAnnotation() {

        val resolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata!!)
                .annotationTypes(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class)
                .build()

        val factoryClasses = resolver.getAttributeValueAsArray(
                "authenticationEntryPointFactory", AuthenticationEntryPointFactory::class
        )
        val factoryClass = factoryClasses!![0]

        Assertions.assertThat(factoryClasses.size).isEqualTo(1)
        Assertions.assertThat(factoryClass).isEqualTo(SecondTestTimeEntryPointFactory::class)
    }
}

class TestingGeneralImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationMetadataResolverGeneralFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods(authenticationEntryPointFactory = [SecondTestTimeEntryPointFactory::class])
@SimpleBasicAuthentication
@Import(TestingGeneralImportBeanDefinitionRegistrar::class)
class GeneralTestConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
