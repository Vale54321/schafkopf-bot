package de.heiserer.cards

class SortedCardList(private val gameType: GameType, withAllCards: Boolean = false) : UnsortedCardList(withAllCards) {
    override fun add(card: Card) {
        super.add(card)
        sort()
    }

    fun getCardsWithoutTrumpf(color: CardColor? = null): SortedCardList {
        val cardsWithoutTrumpf = SortedCardList(gameType)

        color?.let { cardsWithoutTrumpf.add(get(it)) }?: cardsWithoutTrumpf.add(this)
        try {
            cardsWithoutTrumpf.remove(getTrumpf())
        } catch (_: IllegalArgumentException) {
        }

        return cardsWithoutTrumpf
    }

    fun getTrumpf(): SortedCardList {
        val trumpf = SortedCardList(gameType)

        gameType.symbol?.let {
            trumpf.add(get(it))
        } ?: run {
            trumpf.add(get(CardSymbol.OBER))
            trumpf.add(get(CardSymbol.UNTER))
        }

        gameType.color?.let {
            try{ trumpf.add(get(it)) } catch (_: CardAlreadyAddedException) {}
        }

        return trumpf
    }

    private fun sort() {
        super.sortInternal(gameType.symbol, gameType.color)
    }
}