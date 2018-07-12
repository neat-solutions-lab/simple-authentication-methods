package nsl.sam.authenticator

import nsl.sam.authenticator.localtokens.UserAndRoles

interface TokenAuthenticator {

    fun mapToUser(token:String): UserAndRoles

}