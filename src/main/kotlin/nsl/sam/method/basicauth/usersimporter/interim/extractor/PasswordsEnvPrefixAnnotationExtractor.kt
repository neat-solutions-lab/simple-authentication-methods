package nsl.sam.method.basicauth.usersimporter.interim.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.importer.AnnotationEnvPrefixExtractor
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor

class PasswordsEnvPrefixAnnotationExtractor : AnnotationEnvPrefixExtractor {

    override fun getCredentialsEnvironmentVariablePrefix(attributes: EnableAnnotationAttributes): Array<String> {

        val simpleBasicAuthenticationAttributes = SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                attributes.enableAnnotationMetadata
        )

        val envVariablePrefix = simpleBasicAuthenticationAttributes.usersEnvPrefix
        val envVarsAccessor = SteeredEnvironmentVariablesAccessor()
        return if (envVariablePrefix.isNotBlank()) {
            val allEnvVars = envVarsAccessor.getVarsMap()
            val filteredEnvVars = allEnvVars.filter { it.key.startsWith(envVariablePrefix) }
            filteredEnvVars.values.toTypedArray()
        } else {
            emptyArray()
        }
    }
}