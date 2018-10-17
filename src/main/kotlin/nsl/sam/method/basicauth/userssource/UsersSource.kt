package nsl.sam.method.basicauth.userssource

import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.method.basicauth.domain.user.UserTraits

interface UsersSource : ItemsAvailabilityAware {
    fun getUserTraits(username: String): UserTraits
}