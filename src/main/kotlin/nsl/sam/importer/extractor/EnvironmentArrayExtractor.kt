package nsl.sam.importer.extractor

import nsl.sam.core.annotation.EnableAnnotationAttributes

interface EnvironmentArrayExtractor {
    fun getArrayFromEnvVars(attributes: EnableAnnotationAttributes): Array<String>
}