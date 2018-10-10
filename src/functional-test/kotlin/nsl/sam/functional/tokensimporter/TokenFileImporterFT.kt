package nsl.sam.functional.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.functional.usersimporter.AnnotationAttributeUsersImporterFT
import nsl.sam.functional.usersimporter.AnnotationAttributeUsersImporterFTConfiguration
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.tokensimporter.factory.TokenFileImporterFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
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
internal class TokenFileImporterFT {

    @Autowired
    lateinit var environment: Environment

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun test() {

        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)
        val factory = TokenFileImporterFactory()
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
        TokenFileImporterFT.importingClassMetadata = importingClassMetadata
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
