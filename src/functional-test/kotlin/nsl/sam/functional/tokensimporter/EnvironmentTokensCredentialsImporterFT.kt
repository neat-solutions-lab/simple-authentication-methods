package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.envvar.EnvironmentVariablesAccessor
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.domain.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.extractor.TokensArrayEnvVarExtractor
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
        classes = [EnvironmentTokensCredentialsImporterFTConfiguration::class])

@AutoConfigureMockMvc(secure = false)
class EnvironmentTokensCredentialsImporterFT {
    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }


    @Test
    fun environmentTokensImporterPcksUpTokensFromEnvironment() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(
                        importingClassMetadata!!
                )

        val environmentCredentialsReader = EnvironmentCredentialsReader(
                enableAnnotationAttributes, TokensArrayEnvVarExtractor(
                object : EnvironmentVariablesAccessor {
                    override fun getVarsMap(): Map<String, String> {
                        return mapOf(
                                "TestAppTokens.1" to "12345678910 tester10 USER ADMIN ROOT",
                                "TestAppTokens.2" to "12345678911 tester11 USER ADMIN ROOT",
                                "TestAppTokens.3" to "12345678912 tester12 USER ADMIN ROOT"
                        )
                    }
                }
        ))

        val environmentAttributeTokensImporter = TokensCredentialsImporter(environmentCredentialsReader)

        val resolvedTokens = mutableListOf<ResolvedToken>()

        environmentAttributeTokensImporter.reset()
        environmentAttributeTokensImporter.use { importer ->
            for(resolvedToken in importer) {
                println("token: $resolvedToken")
                resolvedTokens.add(resolvedToken)
            }
        }
        Assertions.assertThat(resolvedTokens.size).isEqualTo(3)
        Assertions.assertThat(resolvedTokens).usingElementComparator(ResolvedTokensComparator()).isEqualTo(
                listOf(
                        ResolvedToken("12345678910",
                                "tester10", arrayOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT")),
                        ResolvedToken("12345678911",
                                "tester11", arrayOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT")),
                        ResolvedToken("12345678912",
                                "tester12", arrayOf("ROLE_USER", "ROLE_ADMIN", "ROLE_ROOT"))
                )
        )
    }
}

class EnvironmentTokensCredentialsImporterFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EnvironmentTokensCredentialsImporterFT.importingClassMetadata = importingClassMetadata
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleTokenAuthentication(tokensEnvPrefix = "TestAppTokens")
@Import(EnvironmentTokensCredentialsImporterFTImportBeanDefinitionRegistrar::class)
class EnvironmentTokensCredentialsImporterFTConfiguration