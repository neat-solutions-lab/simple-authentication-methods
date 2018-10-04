package nsl.sam.core.entrypoint.factory

class RemoteCommunicatorFactory: ImaginaryCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return RemoteCommunicator()
    }
}