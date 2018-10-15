package nsl.sam.importer.reader

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.AnnotationCredentialsExtractor
import nsl.sam.importer.CredentialsReader

class AnnotationCredentialsReader (
        attributes: EnableAnnotationAttributes,
        annotationCredentialsExtractor: AnnotationCredentialsExtractor
) : CredentialsReader {

    private val delegatedArrayReader: ArrayCredentialsReader

    init {
        val usersArray = annotationCredentialsExtractor.getCredentialsFromAnnotation(attributes)
        delegatedArrayReader = ArrayCredentialsReader(usersArray)
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