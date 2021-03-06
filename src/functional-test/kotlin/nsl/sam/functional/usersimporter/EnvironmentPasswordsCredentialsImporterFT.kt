package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.envvar.EnvironmentVariablesAccessor
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.extractor.PasswordsArrayEnvVarExtractor
import nsl.sam.utils.UserTraitsComparator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [EnvironmentVariableUsersImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class EnvironmentPasswordsCredentialsImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun readEnvironmentVariableTest() {
        val enableAnnotationAttributes =
                EnableAnnotationAttributesExtractor.extractAttributes(importingClassMetadata!!)

        val environmentCredentialsReader = EnvironmentCredentialsReader(
                enableAnnotationAttributes, PasswordsArrayEnvVarExtractor(
                object : EnvironmentVariablesAccessor {
                    override fun getVarsMap(): Map<String, String> {
                        return mapOf(
                                "TestAppUsers.1" to "environment-user1:{noop}test USER ADMIN",
                                "TestAppUsers.2" to "environment-user2:{noop}test USER ADMIN",
                                "TestAppUsers.3" to "environment-user3:{noop}test USER ADMIN"
                        )
                    }

                }
        ))

        val environmentPasswordsImporter = PasswordsCredentialsImporter(environmentCredentialsReader)

        val resultUserTraits: MutableList<UserTraits> = mutableListOf()

        environmentPasswordsImporter.reset()
        environmentPasswordsImporter.use { importer ->
            for (userTrait in importer) {
                resultUserTraits.add(userTrait)
            }
        }

        Assertions.assertThat(resultUserTraits.size).isEqualTo(3)
        Assertions.assertThat(resultUserTraits).usingElementComparator(UserTraitsComparator()).isEqualTo(
                listOf(
                        UserTraits("environment-user1", "{noop}test", arrayOf("USER", "ADMIN")),
                        UserTraits("environment-user2", "{noop}test", arrayOf("USER", "ADMIN")),
                        UserTraits("environment-user3", "{noop}test", arrayOf("USER", "ADMIN"))
                )
        )
    }
}

class EnvironmentVariableUsersImporterFTConfigurationAnnotationMetadataCatcher : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EnvironmentPasswordsCredentialsImporterFT.importingClassMetadata = importingClassMetadata
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(usersEnvPrefix = "TestAppUsers")
@Import(EnvironmentVariableUsersImporterFTConfigurationAnnotationMetadataCatcher::class)
class EnvironmentVariableUsersImporterFTConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
