package nsl.sam.integration.controller

import nsl.sam.IntegrationTestConstants
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IntegrationTestController {
    @GetMapping(path = [IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT])
    fun testEndpoint(): String = IntegrationTestConstants.FAKE_CONTROLLER_RESPONSE_BODY
}