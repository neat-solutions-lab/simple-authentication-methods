package nsl.sam.functional.controller

import nsl.sam.FunctionalTestConstants
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TwoZonesTestController {

    @GetMapping(FunctionalTestConstants.ZONE_ONE_ENTRY_POINT)
    fun zoneOneEndpoint() = FunctionalTestConstants.ZONE_ONE_ENDPOINT_RESPONSE

    @GetMapping(FunctionalTestConstants.ZONE_TWO_ENTRY_POINT)
    fun zoneTwoEndpoint() = FunctionalTestConstants.ZONE_ONE_ENDPOINT_RESPONSE
}