package nsl.sam.method.basicauth.usersimporter

import nsl.sam.importer.CredentialsImporter
import nsl.sam.importer.reader.CredentialsReader
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.parser.PasswordsParser

class PasswordsCredentialsImporter(reader: CredentialsReader)
    : CredentialsImporter<UserTraits>(PasswordsParser(), reader)