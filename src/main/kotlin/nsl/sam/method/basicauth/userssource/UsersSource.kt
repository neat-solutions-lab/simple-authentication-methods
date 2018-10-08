package nsl.sam.method.basicauth.userssource

import nsl.sam.interfaces.ItemsAvailabilityAware

interface UsersSource: ItemsAvailabilityAware{
    fun getUserPasswordAndRoles(username:String) : Pair<String, Array<String>>

    /**
     * Checks if underlying technology which provides users is configured and able to supply user records,
     * i.e. if users are read from file then the source is considered to be available if the file with users
     * exist and additionally there is at leas one user in the file
     */
    //fun isAvailable(): Boolean

    //fun usersNumber(): Int
}