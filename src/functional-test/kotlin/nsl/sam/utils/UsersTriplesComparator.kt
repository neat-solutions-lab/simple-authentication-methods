package nsl.sam.utils

class UsersTriplesComparator: Comparator<Triple<String, String, Array<String>>> {
    override fun compare(o1: Triple<String, String, Array<String>>,
                         o2: Triple<String, String, Array<String>>): Int {
        if(o1.first != o2.first) return o1.first.compareTo(o2.first)
        if(o1.second != o2.second) return o1.second.compareTo(o2.second)
        if (o1.third.size != o2.third.size) return o1.third.size.compareTo(o2.third.size)
        if(o1.third.contentDeepEquals(o2.third)) return 0
        return 1
    }
}