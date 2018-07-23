package nsl.sam.functional.basicauth

import nsl.sam.FunctionalTestConstants
import nsl.sam.FunctionalTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FakeController {
    @GetMapping(path = [FunctionalTestConstants.MOCK_MVC_TEST_ENDPOINT])
    fun fakeEndpoint(): String = FAKE_CONTROLLER_RESPONSE_BODY
}