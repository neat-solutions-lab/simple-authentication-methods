package nsl.sam.importer

interface CredentialsParser<T> {
    fun parse(credentialsLine: String): T
}