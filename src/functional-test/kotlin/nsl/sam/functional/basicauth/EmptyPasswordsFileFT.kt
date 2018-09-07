package nsl.sam.functional.basicauth

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * The file with passwords is empty
 */
@ExtendWith(SpringExtension::class)
class EmptyPasswordsFileFT {

    @Test
    fun noBasicAuthWhenEmptyPasswordFile() {

    }
}