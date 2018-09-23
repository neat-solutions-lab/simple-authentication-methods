package nsl.sam.method.basicauth.userdetails

interface UsersSource {
    fun getUserPasswordAndRoles(username:String) : Pair<String, Array<String>>
    fun isAvailable(): Boolean
    fun usersNumber(): Int
}