package nsl.sam.functional.controller

import nsl.sam.FunctionalTestConstants
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import org.springframework.security.core.context.SecurityContextHolder

@RestController
class FunctionalTestController {
    @GetMapping(path = [FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT])
    fun fakeEndpoint(): String = FAKE_CONTROLLER_RESPONSE_BODY

    @GetMapping(path = [FunctionalTestConstants.MOCK_MVC_USER_INFO_ENDPOINT])
    fun userInfoEndpoint(): Any {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal
    }
}