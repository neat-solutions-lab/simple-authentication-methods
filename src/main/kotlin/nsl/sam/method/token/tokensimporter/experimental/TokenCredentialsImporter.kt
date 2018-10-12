package nsl.sam.method.token.tokensimporter.experimental

import nsl.sam.importer.CredentialsImporter
import nsl.sam.importer.CredentialsReader
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.experimental.parser.TokensParser

class TokenCredentialsImporter(credentialsReader: CredentialsReader) : CredentialsImporter<ResolvedToken>(
        TokensParser(), credentialsReader
)