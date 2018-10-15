package nsl.sam.importer

import nsl.sam.core.annotation.EnableAnnotationAttributes

interface AnnotationEnvPrefixExtractor {
    fun getCredentialsEnvironmentVariablePrefix(attributes: EnableAnnotationAttributes): Array<String>
}