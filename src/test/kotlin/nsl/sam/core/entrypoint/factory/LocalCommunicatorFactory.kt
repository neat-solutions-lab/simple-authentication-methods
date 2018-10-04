package nsl.sam.core.entrypoint.factory

class LocalCommunicatorFactory: ImaginayCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return LocalCommunicator()
    }
}