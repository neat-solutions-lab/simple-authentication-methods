package nsl.sam.importer.reader.impl

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.extractor.EnvironmentArrayExtractor
import nsl.sam.importer.reader.CredentialsReader

class EnvironmentCredentialsReader(
        attributes: EnableAnnotationAttributes,
        environmentArrayExtractor: EnvironmentArrayExtractor
) : CredentialsReader {

    private val delegatedArrayReader: ArrayCredentialsReader

    init {
        val environmentUsers = environmentArrayExtractor.getArrayFromEnvVars(
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