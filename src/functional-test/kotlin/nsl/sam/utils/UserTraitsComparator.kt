package nsl.sam.utils

import nsl.sam.method.basicauth.domain.user.UserTraits

class UserTraitsComparator : Comparator<UserTraits> {

    override fun compare(o1: UserTraits, o2: UserTraits): Int {
        if(o1.name != o2.name) return o1.name.compareTo(o2.name)
        if(o1.password != o2.password) return o2.password.compareTo(o2.password)
        if(o1.roles.size != o2.roles.size) return o1.roles.size.compareTo(o2.roles.size)
        if(o1.roles.contentDeepEquals(o2.roles)) return 0
        return 1
    }

//    override fun compare(o1: Comparator<UserTraits>,
//                         o2: Comparator<UserTraits>): Int {
//        if (o1. != o2.first) return o1.first.compareTo(o2.first)
//        if (o1.second != o2.second) return o1.second.compareTo(o2.second)
//        if (o1.third.size != o2.third.size) return o1.third.size.compareTo(o2.third.size)
//        if (o1.third.contentDeepEquals(o2.third)) return 0
//        return 1
//    }
}