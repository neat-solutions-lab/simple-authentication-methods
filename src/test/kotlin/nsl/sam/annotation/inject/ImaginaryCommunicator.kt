package nsl.sam.annotation.inject

import java.time.Instant

interface ImaginaryCommunicator {

    fun send(msg:String): Instant

}