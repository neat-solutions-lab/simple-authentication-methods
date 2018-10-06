package nsl.sam.functional.entrypointfactory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.annotation.inject.FactoryRetriever
import nsl.sam.annotation.inject.SingletonObjectFactoryWrapper
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [FactoryRetrievalTestConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class FactoryRetrieverFT{

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Autowired
    lateinit var environment: Environment

    @Test
    fun twoTimesTheSameCustomFactoryRetrieved() {
        val resolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata!!)
                .annotationTypes(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class)
                .build()

        val factory1 = FactoryRetriever.getFactory(
                AuthenticationEntryPointFactory::class,
                "authenticationEntryPointFactory",
                resolver,
                environment,
                "nsl.sam.authentication-entry-point.factory",
                DefaultAuthenticationEntryPointFactory::class
        )

        val factory2 = FactoryRetriever.getFactory(
                AuthenticationEntryPointFactory::class,
                "authenticationEntryPointFactory",
                resolver,
                environment,
                "nsl.sam.authentication-entry-point.factory",
                DefaultAuthenticationEntryPointFactory::class
        )

        val wrapper1 = factory1 as SingletonObjectFactoryWrapper
        val wrapper2 = factory2 as SingletonObjectFactoryWrapper

        val wrapped1 = ReflectionTestUtils.getField(wrapper1, "wrappedFactory")
        val wrapped2 = ReflectionTestUtils.getField(wrapper2, "wrappedFactory")

        val field1 = ReflectionTestUtils.getField(wrapper1, "createdInstance")
        val field2 = ReflectionTestUtils.getField(wrapper2, "createdInstance")

        Assertions.assertThat(factory1).isSameAs(factory2)
        Assertions.assertThat(wrapped1).isSameAs(wrapped2)
        Assertions.assertThat(field1).isSameAs(field2)
    }

    @Test
    fun factoryEachTimeReturnsTheSameTargetObject() {
        val resolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata!!)
                .annotationTypes(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class)
                .build()

        val factory = FactoryRetriever.getFactory(
                AuthenticationEntryPointFactory::class,
                "authenticationEntryPointFactory",
                resolver,
                environment,
                "nsl.sam.authentication-entry-point.factory",
                DefaultAuthenticationEntryPointFactory::class
        )

        val authenticationEntryPoint1 = factory.create()
        val authenticationEntryPoint2 = factory.create()

        Assertions.assertThat(authenticationEntryPoint1).isSameAs(authenticationEntryPoint2)
        println("authenticationEntryPoint1: $authenticationEntryPoint1")
        println("authenticationEntryPoint2: $authenticationEntryPoint2")
    }
}

class TestingFactoryRetrievalImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        FactoryRetrieverFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods(authenticationEntryPointFactory = [SecondTestTimeEntryPointFactory::class])
@SimpleBasicAuthentication(authenticationEntryPointFactory = [FirstTestTimeEntryPointFactory::class])
@Import(TestingFactoryRetrievalImportBeanDefinitionRegistrar::class)
class FactoryRetrievalTestConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
