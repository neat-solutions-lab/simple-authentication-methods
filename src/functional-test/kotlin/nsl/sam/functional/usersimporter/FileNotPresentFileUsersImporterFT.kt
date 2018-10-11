package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.factory.FileUsersImporterFactory
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
        classes = [FileNotPresentFileUsersImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class FileNotPresentFileUsersImporterFT {
    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Autowired
    lateinit var environment: Environment

    lateinit var importer: UsersImporter

    @BeforeEach
    fun createFileTokensImporter() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        val factory = FileUsersImporterFactory()
        importer = factory.create(enableAnnotationAttributes, environment)
    }

    @Test
    fun zeroUsersImportedFromImporterWhenPasswordsFileDoesNotExist() {

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
    fun hasItemsReturnsFalseWhenPasswordsFileIsAbsent() {
        Assertions.assertThat(importer.hasItems()).isEqualTo(false)
    }
}

class FileNotPresentFileUsersImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        FileNotPresentFileUsersImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(passwordsFilePath= "src/functional-test/config/absent-passwords.conf")
@Import(FileNotPresentFileUsersImporterFTImportBeanDefinitionRegistrar::class)
class FileNotPresentFileUsersImporterFTConfiguration