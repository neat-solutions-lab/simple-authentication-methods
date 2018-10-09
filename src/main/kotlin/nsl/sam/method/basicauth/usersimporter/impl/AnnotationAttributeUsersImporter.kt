package nsl.sam.method.basicauth.usersimporter.impl

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.parser.BasicUserLineParser

/**
 * Imports users provided explicitly with `users` attribute of the
 * [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication] annotation.
 */
class AnnotationAttributeUsersImporter(val attributes: EnableAnnotationAttributes) : UsersImporter {

    private var currentIndex = 0
    private var usersArray: Array<String>

    init {
        val simpleBasicAuthenticationAttributes =
                SimpleBasicAuthenticationAttributesExtractor.extractAttributes(attributes.enableAnnotationMetadata)
        usersArray = simpleBasicAuthenticationAttributes.users
    }

    override fun close() {
        // Nothing to do here
    }

    override fun reset() {
        currentIndex = 0
    }

    override fun hasItems(): Boolean {
        return usersArray.isNotEmpty()
    }

    override fun hasNext(): Boolean {
        return usersArray.size > currentIndex
    }

    override fun next(): Triple<String, String, Array<String>> {
        val userDataAsRawString = usersArray[currentIndex++]
        return BasicUserLineParser.parseToTriple(userDataAsRawString)
    }
}