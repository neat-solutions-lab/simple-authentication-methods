package nsl.sam.userdetails

interface UsersSource {
    fun getUserPasswordAndRoles(username:String) : Pair<String, Array<String>>
}