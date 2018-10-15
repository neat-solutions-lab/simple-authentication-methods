package nsl.sam.method.basicauth.usersimporter.interim.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.AnnotationCredentialsExtractor
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor

class PasswordsAnnotationCredentialsExtractor : AnnotationCredentialsExtractor {

    override fun getCredentialsFromAnnotation(attributes: EnableAnnotationAttributes): Array<String> {
        val simpleBasicAuthenticationAttributes =
                SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                        attributes.enableAnnotationMetadata
                )
        return simpleBasicAuthenticationAttributes.users

    }

}