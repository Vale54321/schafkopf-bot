package de.heiserer.player

import de.heiserer.cards.*

class NPCPlayer(name: String) : Player(name){
    override fun playCard(tableCards: UnsortedCardList, gameType: GameType): Card {
        if(tableCards.size() == 0){
            return playTrumpf()
        }

        val firstCard = tableCards.get(0)
        return if(CardToolkit.isTrumpf(gameType, firstCard)){
            playTrumpf()
        } else {
            playColor(firstCard.color)
        }
    }

    private fun playTrumpf(): Card {
        val trumpfCards = cards.getTrumpf()
        if (trumpfCards.size() > 0) {
            val card = trumpfCards.removeLast()
            return cards.remove(card)
        } else {
            return cards.removeFirst()
        }
    }

    private fun playColor(color: CardColor): Card {
        val colorCards = cards.getCardsWithoutTrumpf(color)
        if (colorCards.size() > 0) {
            val card = colorCards.removeLast()
            return cards.remove(card)
        } else {
            return cards.removeFirst()
        }
    }
}