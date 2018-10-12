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

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [DuplicateTokensFileTokensImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class DuplicateTokensFileTokensImporterFT {

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

    /*
     * it is not TokensImporter business to deal with duplicates.
     * It is to be handled by TokensResolver which uses TokensImporter
     */
    @Test
    fun duplicateTokensAreJustPassedThrough() {
        var tokensNumber = 0

        val tokensSet: MutableSet<String> = mutableSetOf()

        importer.reset()
        importer.use {
            for(token in it) {
                println("token: $token")
                tokensNumber++
                tokensSet.add(token.tokenValue)
            }
        }

        Assertions.assertThat(tokensNumber).isEqualTo(6)
        Assertions.assertThat(tokensSet.size).isEqualTo(5)
    }
}

class DuplicateTokensFileTokensImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        DuplicateTokensFileTokensImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokensFilePath = "src/functional-test/config/duplicate-tokens.conf")
@Import(DuplicateTokensFileTokensImporterFTImportBeanDefinitionRegistrar::class)
class DuplicateTokensFileTokensImporterFTConfiguration