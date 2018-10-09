package nsl.sam.method.basicauth.userssource

import nsl.sam.interfaces.ItemsAvailabilityAware

interface UsersSource : ItemsAvailabilityAware {
    fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>>
}