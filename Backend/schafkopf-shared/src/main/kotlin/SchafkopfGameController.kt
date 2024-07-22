package de.heiserer

import card.CardList
import card.CardToolkit
import de.heiserer.cards.*
import de.heiserer.player.Player

class SchafkopfGameController(palyers: List<Player>, private val messager: SchafkopfMessager) {
    private val players = palyers

    private lateinit var gameType: GameType
    private val playedCards: CardList = UnsortedCardList()

    fun playRound(startingOffset: Int = 0){
        serveCards()

        gameType = GameType.SAU_SPIEL
        players.forEach { it.sortCards(gameType) }

        var startingPlayer: Player = players[startingOffset % 4]

        for(i in 0 until 8){
            startingPlayer = playTrick(startingPlayer)
            messager.sendPlayerWonTrick(startingPlayer)
        }
    }

    private fun playTrick(startingPlayer: Player): Player{
        val tableCards = UnsortedCardList()

        for(i in 0 until 4){
            val currentPlayer = calculatePlayerOffset(startingPlayer, i)
            messager.sendPlayerTurn(currentPlayer)
            val card = currentPlayer.playCard(tableCards, gameType)
            messager.sendCardPlayed(currentPlayer, card)
            tableCards.add(card)
            messager.sendTableCards(tableCards.getCopyOfCards())
        }

        playedCards.add(tableCards)

        val trickOffset = CardToolkit.whoTricks(gameType, tableCards)
        return calculatePlayerOffset(startingPlayer, trickOffset)
    }

    private fun calculatePlayerOffset(startingPlayer: Player, i: Int): Player = players[(players.indexOf(startingPlayer) + i) % 4]

    private fun serveCards(){
        val deck = UnsortedCardList(true)
        deck.shuffle()

        for(i in 0 until 4){
            val playerCards = UnsortedCardList()

            for(j in 0 until 8){
                playerCards.add(deck.removeLast())
            }

            players[i % 4].serveCards(playerCards)
        }
    }
}
