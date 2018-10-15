package nsl.sam.importer.reader

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ArrayCredentialsReaderTest {

    @Test
    fun readOneCredentialWhenArrayWithOneCredentialProvided() {

        val credentialsArray = arrayOf("credential1")
        val credentialsReader = ArrayCredentialsReader(credentialsArray)

        var credentialsCounter = 0

        while(true) {
            val credentials = credentialsReader.readCredentials()
            println("credentials: $credentials")
            if(credentials==null) break
            credentialsCounter++
        }

        Assertions.assertThat(credentialsCounter).isEqualTo(1)
    }

    @Test
    fun readZeroCredentialsWhenEmptyArrayOfCredentialsProvided() {

        val emptyArray: Array<String> = emptyArray()
        val credentialsReader = ArrayCredentialsReader(emptyArray)

        var credentialsCounter = 0

        while (true) {
            val credentials = credentialsReader.readCredentials()
            println("credential: $credentials")
            if(null == credentials) break
            credentialsCounter++
        }

        Assertions.assertThat(credentialsCounter).isEqualTo(0)
    }

    @Test
    fun readThreeCredentialsWhenArrayWithThreeCredentialsProvided() {
        val credentialsArray: Array<String> = arrayOf("credential1", "credential2", "credential3")
        val credentialsReader = ArrayCredentialsReader(credentialsArray)

        var credentialsCounter = 0

        while (true) {
            val credentials = credentialsReader.readCredentials()
            println("credential: $credentials")
            if(null == credentials) break
            credentialsCounter++
        }

        Assertions.assertThat(credentialsCounter).isEqualTo(3)
    }

    @Test
    fun ommitCredentialWhichStartWithHashSign() {
        val credentialsArray: Array<String> = arrayOf(
                "credential01", "credential02", "credential03",
                "#credential04", "#credential05", "#credential06",
                "  #credential07", " #credential08", "   #credential09",
                "credential10", "credential11", "credential12"
        )
        val credentialsReader = ArrayCredentialsReader(credentialsArray)

        var credentialsCounter = 0

        while (true) {
            val credentials = credentialsReader.readCredentials()
            println("credential: $credentials")
            if(null == credentials) break
            credentialsCounter++
        }

        Assertions.assertThat(credentialsCounter).isEqualTo(6)
    }


}