package nsl.sam.envvar

import java.util.function.Supplier

class TestingEnvVarsSupplier : Supplier<Map<String, String>> {

    override fun get(): Map<String, String> {
        return mapOf(
                "TestAppUsers.1" to "environment-user1:{noop}test USER ADMIN",
                "TestAppUsers.2" to "environment-user2:{noop}test USER ADMIN",
                "TestAppUsers.3" to "environment-user3:{noop}test USER ADMIN"
        )
    }
}