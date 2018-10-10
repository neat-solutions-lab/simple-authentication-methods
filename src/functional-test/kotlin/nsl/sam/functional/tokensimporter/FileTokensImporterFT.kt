package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.tokensimporter.factory.FileTokenImporterFactory
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

    @Autowired
    lateinit var environment: Environment

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun test() {

        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        val factory = FileTokenImporterFactory()
        val importer = factory.create(enableAnnotationAttributes, environment)

        importer.reset()
        importer.use {
            for(token in it) {
                println("token: $token")
            }
        }
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
//{
//    @Bean
//    fun customAuthorizationTestController() = CustomAuthorizationTestController()
//}
