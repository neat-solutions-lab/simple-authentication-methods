package nsl.sam.importer.reader

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.AnnotationEnvPrefixExtractor
import nsl.sam.importer.CredentialsReader

class EnvironmentCredentialsReader(
        attributes: EnableAnnotationAttributes,
        annotationEnvPrefixExtractor: AnnotationEnvPrefixExtractor
) : CredentialsReader {

    private val delegatedArrayReader: ArrayCredentialsReader

    init {
        val environmentUsers = annotationEnvPrefixExtractor.getCredentialsEnvironmentVariablePrefix(
                attributes
        )
        delegatedArrayReader = ArrayCredentialsReader(environmentUsers)
    }

    override fun readCredentials(): String? {
        return delegatedArrayReader.readCredentials()
    }

    override fun close() {
        return delegatedArrayReader.close()
    }

    override fun reset() {
        return delegatedArrayReader.reset()
    }
}