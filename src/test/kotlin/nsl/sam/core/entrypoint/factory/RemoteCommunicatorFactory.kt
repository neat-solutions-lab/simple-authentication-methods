package nsl.sam.core.entrypoint.factory

class RemoteCommunicatorFactory: ImaginayCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return RemoteCommunicator()
    }
}