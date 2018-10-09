package nsl.sam.functional.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomAuthorizationTestController {
    @GetMapping("/admin-area")
    fun adminArea() = "Hello from Admin area!"

    @GetMapping("/user-area")
    fun userArea() = "Hello from User area!"
}