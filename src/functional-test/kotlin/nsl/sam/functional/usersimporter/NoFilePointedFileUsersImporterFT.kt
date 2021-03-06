package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.factory.FilePasswordCredentialsImporterFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [NoFilePointedFileUsersImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class NoFilePointedFileUsersImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }
    @Autowired
    lateinit var environment: Environment

    lateinit var importer: PasswordsCredentialsImporter

    @BeforeEach
    fun createFileTokensImporter() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        val factory = FilePasswordCredentialsImporterFactory()
        importer = factory.create(enableAnnotationAttributes, environment)
    }

    @Test
    fun zeroUsersImportedFromImporterWhenPasswordsFileNotPointed() {

        var usersCounter = 0

        importer.reset()
        importer.use {
            for (user in it) {
                usersCounter++
            }
        }

        Assertions.assertThat(usersCounter).isEqualTo(0)
    }

    @Test
    fun hasItemsReturnsFalseWhenNoPasswordsFileIsPointed() {
        Assertions.assertThat(importer.hasItems()).isEqualTo(false)
    }
}

class NoFilePointedFileUsersImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        NoFilePointedFileUsersImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@Import(NoFilePointedFileUsersImporterFTImportBeanDefinitionRegistrar::class)
class NoFilePointedFileUsersImporterFTConfiguration