package nsl.sam.annotation.inject

class ItJokesDispenserFactory: ImaginaryJokesDispenserFactory {
    override fun create(): ImaginaryJokesDispenser {
        return ItJokesDispenser()
    }
}