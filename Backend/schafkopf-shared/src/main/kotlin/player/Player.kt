package de.heiserer.player

import de.heiserer.cards.*

abstract class Player(private var name: String){
    protected var cards: SortedCardList = SortedCardList(GameType.SAU_SPIEL)

    fun serveCards(cards: UnsortedCardList){
        this.cards = cards.asSortedCardList(GameType.SAU_SPIEL)
    }

    fun sortCards(gameType: GameType){
        cards = cards.asSortedCardList(gameType)
    }

    abstract fun playCard(tableCards: UnsortedCardList, gameType: GameType): Card

    fun getName(): String{
        return name
    }

    protected fun validateCard(card: Card, tableCards: UnsortedCardList, gameType: GameType): Boolean{
        if(tableCards.size() == 0){
            return true
        }

        val firstCard = tableCards.get(0)

        return if(CardToolkit.isTrumpf(gameType, firstCard)){
            CardToolkit.isTrumpf(gameType, card) || cards.getTrumpf().size() == 0
        } else {
            card.color == firstCard.color || cards.getCardsWithoutTrumpf(firstCard.color).size() == 0
        }
    }
}