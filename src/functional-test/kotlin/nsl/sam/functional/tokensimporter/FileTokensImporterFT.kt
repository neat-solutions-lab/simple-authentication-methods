package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.token.ResolvedToken
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
        classes = [TokenFileImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
internal class FileTokensImporterFT {

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
    fun importerProvidesOneWellKnownToken() {

        var resolvedToken: ResolvedToken? = null
        var tokensNumber = 0

        importer.reset()
        importer.use {
            for(token in it) {
                println("token: $token")
                resolvedToken = token
                tokensNumber++
            }
        }

        Assertions.assertThat(tokensNumber).isEqualTo(1)
        Assertions.assertThat(resolvedToken?.tokenValue).isEqualTo("12345")
        Assertions.assertThat(resolvedToken?.userAndRole?.roles).hasSameElementsAs(
                listOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT")
        )
    }

    @Test
    fun hasItemsReturnsTrue() {
        Assertions.assertThat(importer.hasItems()).isEqualTo(true)
    }

}

internal class TokenFileImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        FileTokensImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokensFilePath = "src/functional-test/config/tokens.conf")
@Import(TokenFileImporterFTImportBeanDefinitionRegistrar::class)
internal class TokenFileImporterFTConfiguration
