package nsl.sam.method.basicauth.usersimporter.interim

import nsl.sam.importer.CredentialsImporter
import nsl.sam.importer.CredentialsReader
import nsl.sam.method.basicauth.usersimporter.interim.parser.PasswordsParser

class PasswordsCredentialsImporter(reader: CredentialsReader)
    : CredentialsImporter<Triple<String, String, Array<String>>>(PasswordsParser(), reader)