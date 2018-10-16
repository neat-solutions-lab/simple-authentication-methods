package nsl.sam.method.token.tokensimporter.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.extractor.AnnotationArrayExtractor
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttributesExtractor

class TokensArrayAnnotationExtractor : AnnotationArrayExtractor {

    override fun getArrayFromAnnotation(attributes: EnableAnnotationAttributes): Array<String> {

        val tokenAuthenticationAttributes =
                SimpleTokenAuthenticationAttributesExtractor.extractAttributes(
                        attributes.enableAnnotationMetadata
                )
        return tokenAuthenticationAttributes.tokens
    }

}