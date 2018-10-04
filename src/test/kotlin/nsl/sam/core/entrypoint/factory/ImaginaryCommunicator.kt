package nsl.sam.core.entrypoint.factory

import java.time.Instant

interface ImaginaryCommunicator {

    fun send(msg:String): Instant

}