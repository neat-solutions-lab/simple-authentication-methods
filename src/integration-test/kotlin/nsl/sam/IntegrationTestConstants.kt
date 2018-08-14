package nsl.sam

object IntegrationTestConstants {
    const val FAKE_CONTROLLER_RESPONSE_BODY = "Integration test sender!"
    const val INTEGRATION_TEST_ENDPOINT = "/integration-test"
    const val EXISTING_BASIC_AUTH_USER_NAME = "tester"
    const val EXISTING_BASIC_AUTH_USER_CORRECT_PASSWORD = "integration"
    const val EXISTING_BASIC_AUTH_USER_INCORRECT_PASSWORD = "incorrect-password"
    const val NOT_EXISTING_BASIC_AUTH_USER_NAME = "not-existing-user"
    const val NOT_EXISTING_BASIC_AUTH_USER_PASSWORD = "any"

    const val TOKEN_AUTH_HEADER_NAME = "Authorization"
    const val TOKEN_AUTH_HEADER_AUTHORIZED_VALUE = "Bearer 12345"
    const val TOKEN_AUTH_HEADER_NOT_AUTHORIZED_VALUE = "Bearer 54321"

}