package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.interim.extractor.PasswordsArrayAnnotationExtractor
import nsl.sam.utils.UsersTriplesComparator
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
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [AnnotationAttributeUsersImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationAttributeUsersImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationAttributeUsersImporterPicksUpUsersFromAnnotationAttribute() {

        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        val annotationCredentialsReader = AnnotationCredentialsReader(
                enableAnnotationAttributes, PasswordsArrayAnnotationExtractor())

        val annotationAttributeUsersImporter = PasswordsCredentialsImporter(annotationCredentialsReader)

        val resultTriples = mutableListOf<Triple<String, String, Array<String>>>()

        annotationAttributeUsersImporter.reset()
        annotationAttributeUsersImporter.use { importer ->
            for (triple in importer) {
                resultTriples.add(triple)
            }
        }

        Assertions.assertThat(resultTriples.size).isEqualTo(2)
        Assertions.assertThat(resultTriples).usingElementComparator(UsersTriplesComparator()).isEqualTo(
                listOf(
                        Triple("hardcoded-user1", "{noop}test", arrayOf("USER", "ADMIN")),
                        Triple("hardcoded-user2", "{noop}test", arrayOf("USER", "ADMIN"))
                )
        )
    }
}

class AnnotationMetadataCatcher : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationAttributeUsersImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(users =
[
    "hardcoded-user1:{noop}test USER ADMIN",
    "hardcoded-user2:{noop}test USER ADMIN"
])
@Import(AnnotationMetadataCatcher::class)
class AnnotationAttributeUsersImporterFTConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
