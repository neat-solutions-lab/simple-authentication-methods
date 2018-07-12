package nsl.sam.authenticator

import nsl.sam.user.UserAndRoles

interface TokenAuthenticator {

    fun mapToUser(token:String):UserAndRoles

}