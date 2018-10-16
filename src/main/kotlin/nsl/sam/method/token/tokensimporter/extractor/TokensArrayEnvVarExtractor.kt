package nsl.sam.method.token.tokensimporter.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.EnvironmentVariablesAccessor
import nsl.sam.importer.extractor.EnvironmentArrayExtractor
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttributesExtractor

class TokensArrayEnvVarExtractor(private val envVarsAccessor: EnvironmentVariablesAccessor)
    : EnvironmentArrayExtractor {

    override fun getArrayFromEnvVars(attributes: EnableAnnotationAttributes): Array<String> {

        val simpleTokenAuthenticationAttributes = SimpleTokenAuthenticationAttributesExtractor.extractAttributes(
                attributes.enableAnnotationMetadata
        )

        val envVariablePrefix = simpleTokenAuthenticationAttributes.tokensEnvPrefix
        return if (envVariablePrefix.isNotBlank()) {
            val allEnvVars = envVarsAccessor.getVarsMap()
            val filteredEnvVars = allEnvVars.filter { it.key.startsWith(envVariablePrefix) }
            filteredEnvVars.values.toTypedArray()
        } else {
            emptyArray()
        }
    }
}