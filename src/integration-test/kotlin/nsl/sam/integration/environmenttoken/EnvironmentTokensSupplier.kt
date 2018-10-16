package nsl.sam.integration.environmenttoken

import java.util.function.Supplier

class EnvironmentTokensSupplier : Supplier<Map<String, String>> {
    override fun get(): Map<String, String> {
        return mapOf(
                "SMS_TESTS_TOKEN.0" to "EnvironmentToken001 environment-tester001 USER ADMIN ROOT",
                "SMS_TESTS_TOKEN.1" to "#EnvironmentToken002 environment-tester002 USER ADMIN ROOT",
                "SMS_TESTS_TOKEN.2" to "EnvironmentToken003 environment-tester003 USER ADMIN ROOT"
        )
    }
}