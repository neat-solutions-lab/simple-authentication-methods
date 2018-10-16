package nsl.sam.method.basicauth.usersimporter

import nsl.sam.importer.CredentialsImporter
import nsl.sam.importer.reader.CredentialsReader
import nsl.sam.method.basicauth.usersimporter.parser.PasswordsParser

class PasswordsCredentialsImporter(reader: CredentialsReader)
    : CredentialsImporter<Triple<String, String, Array<String>>>(PasswordsParser(), reader)