package nsl.sam.utils

import nsl.sam.method.token.domain.token.ResolvedToken

class ResolvedTokensComparator : Comparator<ResolvedToken> {
    override fun compare(o1: ResolvedToken, o2: ResolvedToken): Int {
        if(o1.tokenValue != o2.tokenValue) return o1.tokenValue.compareTo(o2.tokenValue)
        if(o1.userName != o2.userName) return o1.userName.compareTo(o2.userName)
        if(o1.roles.contentDeepEquals(o2.roles)) return 0
        return 1
    }
}