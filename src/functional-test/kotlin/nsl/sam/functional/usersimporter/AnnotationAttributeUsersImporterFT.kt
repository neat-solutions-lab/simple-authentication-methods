package nsl.sam.functional.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.functional.controller.CustomAuthorizationTestController
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.usersimporter.impl.AnnotationAttributeUsersImporter
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
        classes = [AnnotationAttributeUsersImporterFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class AnnotationAttributeUsersImporterFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Test
    fun annotationAttributeUsersImporterPicksUpUsersFromAnnotationAttribute() {
        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.extrectAttributes(importingClassMetadata!!)
        val annotationAttributeUsersImporter = AnnotationAttributeUsersImporter(enableAnnotationAttributes)

        val resultTriples = mutableListOf<Triple<String, String, Array<String>>>()

        annotationAttributeUsersImporter.reset()
        annotationAttributeUsersImporter.use { importer ->
            for(triple in importer) {
                resultTriples.add(triple)
            }
        }

        Assertions.assertThat(resultTriples.size).isEqualTo(2)
        Assertions.assertThat(resultTriples).usingElementComparator(UsersTriplesComparator()).isEqualTo(
                listOf(
                        Triple("hardcoded-user1", "{noop}test", arrayOf("USER", "ADMIN")),
                        Triple("hardcoded-user2", "{noop}test", arrayOf("USER", "ADMIN"))
                )
        )
    }
}

class UsersTriplesComparator: Comparator<Triple<String, String, Array<String>>> {
    override fun compare(o1: Triple<String, String, Array<String>>,
                         o2: Triple<String, String, Array<String>>): Int {
        if(o1.first != o2.first) return o1.first.compareTo(o2.first)
        if(o1.second != o2.second) return o1.second.compareTo(o2.second)
        if (o1.third.size != o2.third.size) return o1.third.size.compareTo(o2.third.size)
        if(o1.third.contentDeepEquals(o2.third)) return 0
        return 1
    }
}

class AnnotationMetadataCatcher: ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        AnnotationAttributeUsersImporterFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(users=
[
    "hardcoded-user1:{noop}test USER ADMIN",
    "hardcoded-user2:{noop}test USER ADMIN"
])
@Import(AnnotationMetadataCatcher::class)
class AnnotationAttributeUsersImporterFTConfiguration {
    @Bean
    fun customAuthorizationTestController() = CustomAuthorizationTestController()
}
