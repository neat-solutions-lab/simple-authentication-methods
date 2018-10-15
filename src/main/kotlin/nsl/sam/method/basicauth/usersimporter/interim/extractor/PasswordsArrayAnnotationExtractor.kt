package nsl.sam.method.basicauth.usersimporter.interim.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.extractor.AnnotationArrayExtractor
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor

class PasswordsArrayAnnotationExtractor : AnnotationArrayExtractor {

    override fun getArrayFromAnnotation(attributes: EnableAnnotationAttributes): Array<String> {
        val simpleBasicAuthenticationAttributes =
                SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                        attributes.enableAnnotationMetadata
                )
        return simpleBasicAuthenticationAttributes.users

    }

}