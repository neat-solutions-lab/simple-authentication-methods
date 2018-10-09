package nsl.sam.method.basicauth.annotation

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import org.springframework.core.type.AnnotationMetadata

object SimpleBasicAuthenticationAttributesExtractor {

    fun extractAttributes(annotationMetadata: AnnotationMetadata): SimpleBasicAuthenticationAttributes {
        val annotationMetadataResolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(annotationMetadata)
                .annotationTypes(SimpleBasicAuthentication::class)
                .build()

        if (!annotationMetadataResolver.isAnnotationPresent()) {
            return SimpleBasicAuthenticationAttributes.default()
        }

        return SimpleBasicAuthenticationAttributes.Builder()
                .passwordFilePathProperty(annotationMetadataResolver.getRequiredAttributeValue(
                        "passwordsFilePropertyName", String::class
                ))
                .passwordFilePath(annotationMetadataResolver.getRequiredAttributeValue(
                        "passwordsFilePath", String::class
                ))
                .authenticationEntryPointFactory(annotationMetadataResolver.getRequiredAttributeAsKClassArray (
                        "authenticationEntryPointFactory",
                        AuthenticationEntryPointFactory::class
                ))
                .usersEnvPrefix(annotationMetadataResolver.getRequiredAttributeValue(
                        "usersEnvPrefix", String::class
                ))
                .users(annotationMetadataResolver.getRequiredAttributeValue(
                        "users", Array<String>::class
                ))
                .build()
    }

}