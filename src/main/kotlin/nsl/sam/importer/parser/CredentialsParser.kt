package nsl.sam.importer.parser

interface CredentialsParser<T> {
    fun parse(credentialsLine: String): T
}