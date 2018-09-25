package nsl.sam

object FunctionalTestConstants {

    const val ZONE_ONE_ENTRY_POINT = "/zone-one"
    const val ZONE_TWO_ENTRY_POINT = "/zone-two"

    const val ZONE_ONE_MATCH = "$ZONE_ONE_ENTRY_POINT/**"
    const val ZONE_TWO_MATCH = "$ZONE_TWO_ENTRY_POINT/**"

    const val ZONE_ONE_ENDPOINT_RESPONSE = "Hello from zone ONE"
    const val ZONE_TWO_ENDPOINT_RESPONSE = "Hello from zone TWO"

    const val FAKE_CONTROLLER_RESPONSE_BODY = "Functional test sender!"
    const val MOCK_MVC_TEST_ENDPOINT = "/functional-test"
    const val EXISTING_BASIC_AUTH_USER_NAME = "test"
    const val EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD = "test"
    const val EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD = "incorrect-password"
    const val NOT_EXISTING_BASIC_AUTH_USER_NAME = "not-existing-user"
    const val NOT_EXISTING_BASIC_AUTH_USER_PASSWORD = "any"

    const val TOKEN_AUTH_HEADER_NAME = "Authorization"
    const val TOKEN_AUTH_HEADER_AUTHORIZED_VALUE = "Bearer 12345"
    const val TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE = "Bearer 54321"

    const val MOCK_MVC_USER_INFO_ENDPOINT = "/user-info"

}