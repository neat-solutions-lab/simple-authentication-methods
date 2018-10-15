package nsl.sam.importer

import nsl.sam.core.annotation.EnableAnnotationAttributes

interface AnnotationCredentialsExtractor {
    fun getCredentialsFromAnnotation(attributes: EnableAnnotationAttributes): Array<String>
}