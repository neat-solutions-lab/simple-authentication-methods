package nsl.sam.user

data class UserAndRoles(val name:String, val roles:Array<String> = arrayOf("ROLE_USER"))
