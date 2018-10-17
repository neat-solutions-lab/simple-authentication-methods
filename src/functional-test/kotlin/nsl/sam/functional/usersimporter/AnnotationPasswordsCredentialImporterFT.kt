package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.extractor.PasswordsArrayAnnotationExtractor
import nsl.sam.utils.UserTraitsComparator
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
class AnnotationPasswordsCredentialImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationPasswordsImporterPicksUpUsersFromAnnotationAttribute() {

        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.
                extractAttributes(importingClassMetadata!!)

        val annotationCredentialsReader = AnnotationCredentialsReader(
                enableAnnotationAttributes, PasswordsArrayAnnotationExtractor())

        val annotationAttributeUsersImporter =
                PasswordsCredentialsImporter(annotationCredentialsReader)

        val resultUserTraits = mutableListOf<UserTraits>()

        annotationAttributeUsersImporter.reset()
        annotationAttributeUsersImporter.use { importer ->
            for (userTraits in importer) {
                resultUserTraits.add(userTraits)
            }
        }

        Assertions.assertThat(resultUserTraits.size).isEqualTo(2)
        Assertions.assertThat(resultUserTraits).usingElementComparator(UserTraitsComparator()).isEqualTo(
                listOf(
                        UserTraits("hardcoded-user1", "{noop}test", arrayOf("USER", "ADMIN")),
                        UserTraits("hardcoded-user2", "{noop}test", arrayOf("USER", "ADMIN"))
                )
        )
    }
}

class AnnotationMetadataCatcher : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationPasswordsCredentialImporterFT.importingClassMetadata = importingClassMetadata
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
