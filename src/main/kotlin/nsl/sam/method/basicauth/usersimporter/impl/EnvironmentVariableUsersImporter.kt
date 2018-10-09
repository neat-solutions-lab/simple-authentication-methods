package nsl.sam.method.basicauth.usersimporter.impl

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.EnvironmentVariablesAccessor
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.parser.BasicUserLineParser

class EnvironmentVariableUsersImporter(
        attributes: EnableAnnotationAttributes,
        envVarsAccessor: EnvironmentVariablesAccessor)
    : UsersImporter {

    private var currentIndex = 0
    private var usersArray = emptyArray<String>()

    init {
        val simpleBasicAuthenticationAttributes =
                SimpleBasicAuthenticationAttributesExtractor.extractAttributes(attributes.enableAnnotationMetadata)

        val envVariablePrefix = simpleBasicAuthenticationAttributes.usersEnvPrefix

        if(envVariablePrefix.isNotBlank()) {
            val allEnvVars = envVarsAccessor.getVarsMap()
            val filteredEnvVars = allEnvVars.filter { it.key!!.startsWith(envVariablePrefix) }
            usersArray = filteredEnvVars.values.toTypedArray()
        }
    }

    override fun close() {
        // nothing to do
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
