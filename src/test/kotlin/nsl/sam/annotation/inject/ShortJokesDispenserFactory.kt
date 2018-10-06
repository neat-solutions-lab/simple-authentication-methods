package nsl.sam.annotation.inject

class ShortJokesDispenserFactory: ImaginaryJokesDispenserFactory {
    override fun create(): ImaginaryJokesDispenser {
        return ShortJokesDispenser()
    }
}