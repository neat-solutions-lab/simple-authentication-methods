package nsl.sam.method.basicauth.usersimporter.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.EnvironmentVariablesAccessor
import nsl.sam.importer.extractor.EnvironmentArrayExtractor
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor

class PasswordsArrayEnvVarExtractor(private val envVarsAccessor: EnvironmentVariablesAccessor) : EnvironmentArrayExtractor {

    override fun getArrayFromEnvVars(attributes: EnableAnnotationAttributes): Array<String> {

        val simpleBasicAuthenticationAttributes = SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                attributes.enableAnnotationMetadata
        )

        val envVariablePrefix = simpleBasicAuthenticationAttributes.usersEnvPrefix
        return if (envVariablePrefix.isNotBlank()) {
            val allEnvVars = envVarsAccessor.getVarsMap()
            val filteredEnvVars = allEnvVars.filter { it.key.startsWith(envVariablePrefix) }
            filteredEnvVars.values.toTypedArray()
        } else {
            emptyArray()
        }
    }
}