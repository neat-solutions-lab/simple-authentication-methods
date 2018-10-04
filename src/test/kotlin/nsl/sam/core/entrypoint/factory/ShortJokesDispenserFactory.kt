package nsl.sam.core.entrypoint.factory

class ShortJokesDispenserFactory: ImaginaryJokesDispenserFactory {
    override fun create(): ImaginaryJokesDispenser {
        return ShortJokesDispenser()
    }
}