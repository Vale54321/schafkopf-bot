package de.heiserer

class SchafkopfGameController {
    private val players = listOf(NPCPlayer("NPC 1"), NPCPlayer("NPC 2"), NPCPlayer("NPC 3"), NPCPlayer("NPC 4"))

    private lateinit var gameType: GameType
    private val playedCards: CardList = UnsortedCardList()

    fun playRound(startingOffset: Int = 0){
        serveCards()

        gameType = GameType.SAU_SPIEL
        players.forEach { it.sortCards(gameType) }

        var startingPlayer: Player = players[startingOffset % 4]

        for(i in 0 until 8){
            startingPlayer = playTrick(startingPlayer)
        }
    }

    private fun playTrick(startingPlayer: Player): Player{
        val tableCards = UnsortedCardList()

        for(i in 0 until 4){
            val currentPlayer = calculatePlayerOffset(startingPlayer, i)
            currentPlayer.printName()

            val card = currentPlayer.playCard(tableCards, gameType)
            tableCards.add(card)
        }

        tableCards.print()

        return startingPlayer
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

abstract class Player(private var name: String){
    protected var cards: SortedCardList = SortedCardList(GameType.SAU_SPIEL)

    fun serveCards(cards: UnsortedCardList){
        this.cards = cards.asSortedCardList(GameType.SAU_SPIEL)
    }

    fun sortCards(gameType: GameType){
        cards = cards.asSortedCardList(gameType)
        println("Spieler $name hat folgende Karten sortiert:")
        cards.print()
        println()
        println("Trumpf:")
        cards.getTrumpf().print()
        println()
        println("Farbe:")
        cards.getCardsWithoutTrumpf().print()
        println()
    }

    abstract fun playCard(tableCards: UnsortedCardList, gameType: GameType): Card

    fun printName(){
        println(name)
    }
}

class NPCPlayer(name: String) : Player(name){
    override fun playCard(tableCards: UnsortedCardList, gameType: GameType): Card {
        if(tableCards.size() == 0){
            println("Erster Spieler")
            val card = cards.removeLast()
            println("Spielt ${card.displayName}")
            println()
            return card
        }

        val firstCard = tableCards.get(0)
        if(cards.farbFreiOrTrumpf(firstCard, gameType)){
            println("farbFrei Or Trumpf")
            return playTrumpf()
        }
    }

    private fun playTrumpf(): Card {
        val trumpf = cards.getTrumpf()
        if (trumpf.size() > 0) {
            println("play Trumpf")
            val card = trumpf.removeLast()
            println("Spielt ${card.displayName}")
            println()
            return cards.remove(card)
        } else {
            println("abspatzen")
            return cards.removeFirst()
        }
    }
}