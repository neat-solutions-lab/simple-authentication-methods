package nsl.sam.functional.entrypointfactory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.annotation.inject.FactoryRetriever
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
class FactoryRetrieverFT: ImportSelector{




    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        val resolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(importingClassMetadata)
                .annotationTypes(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class)
                .build()
//
//
//        FactoryRetriever.getFactory(
//                AuthenticationEntryPointFactory::class,
//                "authenticationEntryPointFactory",
//                resolver,
//
//
//        )
        return emptyArray()
    }


    @Test
    fun test() {

    }

    @Configuration
    @EnableSimpleAuthenticationMethods
    @SimpleBasicAuthentication(authenticationEntryPointFactory = [TestTimeEntryPointFactory::class])
    @Import(FactoryRetrieverFT::class)
    class TestConfiguration {
        @Bean
        fun customAuthorizationTestController() = CustomAuthorizationTestController()
    }
}