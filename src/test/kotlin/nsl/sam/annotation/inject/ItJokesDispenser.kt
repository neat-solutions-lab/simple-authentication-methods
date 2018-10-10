package nsl.sam.annotation.inject

class ItJokesDispenser : ImaginaryJokesDispenser {

    private val jokesList = listOf(
            "Q: What is the biggest lie in the universe? A: I have read and agree to the Terms & Conditions",
            "Q: How does a computer getResolvedToken drunk? A: It takes screenshots.",
            "PATIENT: Doctor, I need your help. I'm addicted to checking my Twitter! DOCTOR: I'm so sorry, I don't follow.",
            "Have you heard of that new band '1023 Megabytes'? They're pretty good, but they don't have a gig yet",
            "Q: Why did the computer show up at work late? A: It had a hard drive.",
            "Q: What do you call a programmer from Finland? A: Nerdic.",
            "Q: Why did the computer go to the dentist? A: Because it had Bluetooth.",
            "Three SQL databases walked into a NoSQL bar. A little while later they walked out because they couldn't find a table"
    )

    override fun getJokes(amount: Int): List<String> {
        return jokesList.subList(0, amount - 1)
    }
}