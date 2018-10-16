package nsl.sam.utils

import nsl.sam.method.token.token.ResolvedToken

class ResolvedTokensComparator : Comparator<ResolvedToken> {
    override fun compare(o1: ResolvedToken, o2: ResolvedToken): Int {
        if(o1.tokenValue != o2.tokenValue) return o1.tokenValue.compareTo(o2.tokenValue)
        if(o1.userAndRole.name != o2.userAndRole.name) return o1.userAndRole.name.compareTo(o2.userAndRole.name)
        if(o1.userAndRole.roles.contentDeepEquals(o2.userAndRole.roles)) return 0
        return 1
    }
}