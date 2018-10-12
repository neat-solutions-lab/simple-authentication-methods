package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.tokensimporter.experimental.TokenCredentialsImporter
import nsl.sam.method.token.tokensimporter.experimental.reader.FileTokenCredentialsImporterFactory
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
        classes = [OnlyCommentsFileTokensImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class OnlyCommentsFileTokensImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Autowired
    lateinit var environment: Environment

    lateinit var importer: TokenCredentialsImporter

    @BeforeEach
    fun createFileTokensImporter() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        //val factory = FileTokenImporterFactory()
        val factory = FileTokenCredentialsImporterFactory()
        importer = factory.create(enableAnnotationAttributes, environment)
    }

    @Test
    fun zeroTokensWhenTokensFileContainsOnlyComments() {
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
    fun hasItemsReturnsFalseWhenTokensFileContainsOnlyComments() {
        Assertions.assertThat(importer.hasItems()).isEqualTo(false)
    }
}

class OnlyCommentsFileTokensImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        OnlyCommentsFileTokensImporterFT.importingClassMetadata = importingClassMetadata
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokensFilePath = "src/functional-test/config/only-comments-tokens.conf")
@Import(OnlyCommentsFileTokensImporterFTImportBeanDefinitionRegistrar::class)
class OnlyCommentsFileTokensImporterFTConfiguration