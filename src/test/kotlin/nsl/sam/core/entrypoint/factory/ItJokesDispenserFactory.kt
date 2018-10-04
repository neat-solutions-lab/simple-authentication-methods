package nsl.sam.core.entrypoint.factory

class ItJokesDispenserFactory: ImaginaryJokesDispenserFactory {
    override fun create(): ImaginaryJokesDispenser {
        return ItJokesDispenser()
    }
}