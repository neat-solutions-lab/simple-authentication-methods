package nsl.sam.functional.envvarscredentialsreader

import java.util.function.Supplier

class EnvironmentVariablesSupplier : Supplier<Map<String, String>> {

    override fun get(): Map<String, String> {
        return mapOf(
                "TestAppUsers.1" to "environment-user1:{noop}test USER ADMIN",
                "TestAppUsers.2" to "environment-user2:{noop}test USER ADMIN",
                "TestAppUsers.3" to "environment-user3:{noop}test USER ADMIN",
                "TestAppUsers.4" to "environment-user4:{noop}test USER ADMIN",
                "TestAppUsers.5" to "#environment-user4:{noop}test USER ADMIN",
                "TestAppUsers.6" to "environment-user4:{noop}test USER ADMIN"
        )
    }
}