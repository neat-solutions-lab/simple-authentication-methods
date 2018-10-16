package nsl.sam.method.token.annotation

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import org.springframework.core.type.AnnotationMetadata

object SimpleTokenAuthenticationAttributesExtractor {

    fun extractAttributes(annotationMetadata: AnnotationMetadata): SimpleTokenAuthenticationAttributes {

        val annotationMetadataResolver = AnnotationMetadataResolver.Builder()
                .annotationMetadata(annotationMetadata)
                .annotationTypes(SimpleTokenAuthentication::class)
                .build()

        if (!annotationMetadataResolver.isAnnotationPresent()) {
            return SimpleTokenAuthenticationAttributes.default()
        }

        return SimpleTokenAuthenticationAttributes.Builder()
                .tokensFilePropertyName(annotationMetadataResolver.getRequiredAttributeValue(
                        "tokensFilePropertyName", String::class
                ))
                .tokensFilePath(annotationMetadataResolver.getRequiredAttributeValue(
                        "tokensFilePath", String::class
                ))
                .tokens(annotationMetadataResolver.getRequiredAttributeValue(
                        "tokens", Array<String>::class
                ))
                .tokensEnvPrefix(annotationMetadataResolver.getRequiredAttributeValue(
                        "tokensEnvPrefix", String::class
                ))
                .authenticationEntryPointFactory(annotationMetadataResolver.getRequiredAttributeAsKClassArray(
                        "authenticationEntryPointFactory", AuthenticationEntryPointFactory::class
                ))
                .build()
    }

}