package de.heiserer

import de.heiserer.cards.Card
import de.heiserer.player.Player

interface SchafkopfMessager {
    fun sendPlayerTurn(player: Player)
    fun sendPlayerWonTrick(player: Player)
    fun sendPlayerWonGame(player: Player)
    fun sendCardPlayed(player: Player, card: Card)
    fun sendTableCards(cards: List<Card>)
}