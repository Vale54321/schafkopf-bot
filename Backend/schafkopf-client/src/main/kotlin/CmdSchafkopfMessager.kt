import de.heiserer.CmdPlayer
import de.heiserer.SchafkopfMessager
import de.heiserer.cards.Card
import de.heiserer.cards.CardColor
import de.heiserer.cards.CardSymbol
import de.heiserer.player.Player

class CmdSchafkopfMessager: SchafkopfMessager {
    override fun sendPlayerTurn(player: Player) {
        if(player !is CmdPlayer){
            clearConsole()
        }

        println("${player.getName()} ist am Zug")
        Thread.sleep(1000)
    }

    override fun sendPlayerWonTrick(player: Player) {
        println("${player.getName()} hat den Stich gewonnen.")
        println()
    }

    override fun sendPlayerWonGame(player: Player) {
        TODO("Not yet implemented")
    }

    override fun sendCardPlayed(player: Player, card: Card) {
        println("${player.getName()} hat ${card.displayName} gespielt.")
    }

    override fun sendTableCards(cards: List<Card>) {
        println("Tischkarten:")
        CmdCard.printCards(cards)

        Thread.sleep(3000)
    }



    class CmdCard(card: Card){
        private val symbolMap = mapOf(
            CardSymbol.SIEBEN to "7",
            CardSymbol.ACHT to "8",
            CardSymbol.NEUN to "9",
            CardSymbol.ZEHN to "10",
            CardSymbol.OBER to "O",
            CardSymbol.UNTER to "U",
            CardSymbol.KOENIG to "K",
            CardSymbol.ASS to "A"
        )

        private val colorMap = mapOf(
            CardColor.HERZ to "♥",
            CardColor.SCHELL to "♦",
            CardColor.EICHEL to "♣",
            CardColor.BLATT to "♠"
        )

        private val symbol = symbolMap[card.symbol]?: "?"
        private val color = colorMap[card.color]?: "?"
        private val colorName = card.color.displayName
        private val symbolName = card.symbol.displayName

        private val cardWidth = 13

        private val symbolLine = "│ ${symbol.padEnd(cardWidth-5)}$symbol │"
        private val colorLine = "│ ${color.padEnd(cardWidth-5)}$color │"

        private val cardLines = listOf(
            "┌───────────┐",
            symbolLine,
            colorLine,
            "│${colorName.padCenter(cardWidth-2)}│",
            "│${symbolName.padCenter(cardWidth-2)}│",
            colorLine,
            symbolLine,
            "└───────────┘"

        )

        fun print(){
            cardLines.forEach {
                println(it)
            }
        }

        fun getCardLines(): List<String> {
            return cardLines
        }

        private fun String.padCenter(totalWidth: Int, padChar: Char = ' '): String {
            if (this.length >= totalWidth) return this
            val padding = totalWidth - this.length
            val padStart = this.length + padding / 2
            return this.padStart(padStart, padChar).padEnd(totalWidth, padChar)
        }

        companion object {
            fun printCards(cards: List<Card>) {
                val lastCardLines = CmdCard(cards.last()).getCardLines()

                val cardLines = cards.map { CmdCard(it).getCardLines() }

                // Loop through the indices of the lines that are present in all cards
                for (i in lastCardLines.indices) {
                    // For other lines, print up to the first 5 characters
                    var line = cardLines.joinToString(" ") { it[i].take(5) }
                    line += lastCardLines[i].drop(5)
                    println(line)
                }
            }
        }

    }

    fun clearConsole() {
        for (i in 1..100) {
            println()
        }
    }

}