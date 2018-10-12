package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.factory.FileTokenCredentialsImporterFactory
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

/**
 * tokens file pointed by the annotation configuration attribute does not exist
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [FileNotPresentFileTokensImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class FileNotPresentFileTokensImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Autowired
    lateinit var environment: Environment

    lateinit var importer: TokensCredentialsImporter

    @BeforeEach
    fun createFileTokensImporter() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        //val factory = FileTokenImporterFactory()
        val factory = FileTokenCredentialsImporterFactory()
        importer = factory.create(enableAnnotationAttributes, environment)
    }

    @Test
    fun zeroTokensWhenTokensFileIsAbsent() {
        var tokensNumber = 0

        importer.reset()
        importer.use {
            for(token in it) {
                println("token: $token")
                tokensNumber++
            }
        }

        Assertions.assertThat(tokensNumber).isEqualTo(0)
    }

    @Test
    fun hasItemsReturnsFalseWhenTokensFileIsAbsent() {
        Assertions.assertThat(importer.hasItems()).isEqualTo(false)
    }
}

class FileNotPresentFileTokensImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        FileNotPresentFileTokensImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokensFilePath = "src/functional-test/config/absent-file-tokens.conf")
@Import(FileNotPresentFileTokensImporterFTImportBeanDefinitionRegistrar::class)
class FileNotPresentFileTokensImporterFTConfiguration