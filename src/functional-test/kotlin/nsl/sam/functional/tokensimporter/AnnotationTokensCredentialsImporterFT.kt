package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.token.UserAndRoles
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.extractor.TokensArrayAnnotationExtractor
import nsl.sam.utils.ResolvedTokensComparator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [AnnotationTokensCredentialsImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationTokensCredentialsImporterFT {
    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationTokensImporterPicksUpTokensFromAnnotationAnnotation() {
        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.
                extractAttributes(importingClassMetadata!!)

        val annotationCredentialsReader = AnnotationCredentialsReader(
                enableAnnotationAttributes, TokensArrayAnnotationExtractor())

        val annotationAttributeTokensImporter =
                TokensCredentialsImporter(annotationCredentialsReader)

        val resolvedTokens = mutableListOf<ResolvedToken>()

        annotationAttributeTokensImporter.reset()
        annotationAttributeTokensImporter.use { importer ->
            for(resolvedToken in importer) {
                println("token: $resolvedToken")
                resolvedTokens.add(resolvedToken)
            }
        }
        Assertions.assertThat(resolvedTokens.size).isEqualTo(2)
        Assertions.assertThat(resolvedTokens).usingElementComparator(ResolvedTokensComparator()).isEqualTo(
                listOf(
                        ResolvedToken("12345678910", UserAndRoles(
                                "tester10", arrayOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT"))),
                        ResolvedToken("12345678911", UserAndRoles(
                                "tester11", arrayOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT")))
                )
        )
    }
}

class AnnotationTokensCredentialsImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationTokensCredentialsImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokens = [
"12345678910 tester10 USER ADMIN ROOT",
"12345678911 tester11 USER ADMIN ROOT"
])
@Import(AnnotationTokensCredentialsImporterFTImportBeanDefinitionRegistrar::class)
class AnnotationTokensCredentialsImporterFTConfiguration