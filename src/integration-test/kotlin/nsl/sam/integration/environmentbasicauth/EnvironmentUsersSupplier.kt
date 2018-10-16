package nsl.sam.integration.environmentbasicauth

import java.util.function.Supplier

class EnvironmentUsersSupplier : Supplier<Map<String, String>> {
    override fun get(): Map<String, String> {
        return mapOf(
                "SMS_TESTS_USER.0" to "environment-tester001:{noop}pass001 USER ADMIN",
                "SMS_TESTS_USER.1" to "#environment-tester002:{noop}pass002 USER ADMIN",
                "SMS_TESTS_USER.2" to "environment-tester003:{noop}pass003 USER ADMIN"
        )
    }
}