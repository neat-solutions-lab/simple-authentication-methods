package nsl.sam.importer.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes

interface AnnotationArrayExtractor {
    fun getArrayFromAnnotation(attributes: EnableAnnotationAttributes): Array<String>
}