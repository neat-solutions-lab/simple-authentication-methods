package nsl.sam.functional.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PrincipalAwareController {

    @GetMapping(path = ["/principal-aware-endpoint"])
    fun principalAwareEndpoint(@AuthenticationPrincipal user: UserDetails?): String {
        return if(user != null) {
            "User is " + user?.username
        } else {
            val authentication = SecurityContextHolder.getContext().authentication
            "User is " + authentication?.principal as String
        }
    }

}