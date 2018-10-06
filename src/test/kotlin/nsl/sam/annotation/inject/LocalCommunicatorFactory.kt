package nsl.sam.annotation.inject

class LocalCommunicatorFactory: ImaginaryCommunicatorFactory {
    override fun create(): ImaginaryCommunicator {
        return LocalCommunicator()
    }
}