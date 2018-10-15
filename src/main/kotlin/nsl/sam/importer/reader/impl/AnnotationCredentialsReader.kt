package nsl.sam.importer.reader.impl

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.extractor.AnnotationArrayExtractor
import nsl.sam.importer.reader.CredentialsReader

class AnnotationCredentialsReader (
        attributes: EnableAnnotationAttributes,
        annotationArrayExtractor: AnnotationArrayExtractor
) : CredentialsReader {

    private val delegatedArrayReader: ArrayCredentialsReader

    init {
        val usersArray = annotationArrayExtractor.getArrayFromAnnotation(attributes)
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