package de.heiserer.cards

class CardToolkit private constructor(private val gameType: GameType) {
    private val sortedCardList = UnsortedCardList(true).asSortedCardList(gameType)

    fun isTrumpf(card: Card): Boolean = card in sortedCardList.getTrumpf().getCopyOfCards()

    fun whoTricks(cards: CardList): Int {
        if(cards.size() != 4){
            throw IllegalArgumentException("Es müssen 4 Karten auf dem Tisch liegen.")
        }

        val sortedCards = cards.asSortedCardList(gameType)

        if(sortedCards.getTrumpf().size() > 0){
            return cards.indexOf(sortedCards.getTrumpf().getLast())
        } else {
            val firstColor = cards.get(0).color
            val colorCards = sortedCards.getCardsWithoutTrumpf(firstColor)

            return cards.indexOf(colorCards.getLast())
        }
    }

    companion object {
        fun isTrumpf(gameType: GameType, card: Card): Boolean {
            val toolkit = CardToolkit(gameType)
            return toolkit.isTrumpf(card)
        }

        fun whoTricks(gameType: GameType, cards: CardList): Int {
            val toolkit = CardToolkit(gameType)
            return toolkit.whoTricks(cards)
        }
    }
}