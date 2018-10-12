package nsl.sam.method.token.tokensimporter

import nsl.sam.importer.CredentialsImporter
import nsl.sam.importer.CredentialsReader
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.parser.TokensParser

class TokensCredentialsImporter(credentialsReader: CredentialsReader) : CredentialsImporter<ResolvedToken>(
        TokensParser(), credentialsReader
)