package nsl.sam.core.entrypoint.factory

class LocalCommunicatorFactory: ImaginaryCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return LocalCommunicator()
    }
}