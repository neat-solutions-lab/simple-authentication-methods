package nsl.sam.authenticator.localtokens

import nsl.sam.authenticator.localtokens.UserAndRoles

interface TokenToUserMapper {

    fun mapToUser(token:String): UserAndRoles

}