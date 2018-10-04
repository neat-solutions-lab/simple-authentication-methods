package nsl.sam.core.entrypoint.factory

class ShortJokesDispenser: ImaginaryJokesDispenser {

    val jokesList = listOf(
            "Q: How many tickles does it take to make a squid laugh? A: Ten-tickles.",
            "Q: What is the best thing about Switzerland? A: I don't know, but the flag is a big plus.",
            "Helvetica and Times New Roman walk into a bar. Got out of here! - shouts the bartender - We don't serve your type."
    )

    override fun getJokes(amount: Int): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}