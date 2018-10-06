package nsl.sam.annotation.inject

class RemoteCommunicatorFactory: ImaginaryCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return RemoteCommunicator()
    }
}