package nsl.sam.functional.tokensimporter

import nsl.sam.method.token.tokensimporter.impl.FileTokensImporter
import org.junit.jupiter.api.Test

class CorruptedTokensFileFileTokensImporterFT {

    @Test
    fun test() {
        // TODO: Finish it after changes to TokensImporter(s)
        val fileTokensImporter = FileTokensImporter("src/functional-test/config/corrupted-tokens.conf")
        fileTokensImporter.reset()
        //fileTokensImporter.use {
        //    for (token in it) {
        //        println("token: $token")
        //    }
        //}

    }

}