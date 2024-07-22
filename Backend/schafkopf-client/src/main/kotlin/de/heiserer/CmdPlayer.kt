package de.heiserer

import de.heiserer.cards.*
import de.heiserer.player.Player
import java.util.*


class CmdPlayer(name: String) : Player(name) {
    private val scanner = Scanner(System.`in`)

    override fun playCard(tableCards: UnsortedCardList, gameType: GameType): Card {
        val cardsCopy = cards.getCopyOfCards()

        println("Available cards:")
        CmdSchafkopfMessager.CmdCard.printCards(cardsCopy)

        // Print cards with their respective index
        cardsCopy.forEachIndexed { index, card ->
            println("(${index + 1}) ${card.name}") // Prints card with index starting from 1
        }

        // Prompt the user to enter a card number
        println("Please enter the number of the card you want to play:")

        val userInput = scanner.nextLine()
        val cardToPlay = getCardByUserInput(userInput, tableCards, gameType)

        return cards.remove(cardToPlay)
    }

    private fun getCardByUserInput(
        userInput: String,
        tableCards: UnsortedCardList,
        gameType: GameType
    ): Card {
        val cardsCopy = cards.getCopyOfCards()
        return try {
            // Convert user input to an integer and adjust for zero-based index
            val cardIndex = userInput.toInt() - 1

            // Ensure the index is within bounds
            if (cardIndex in cardsCopy.indices) {
                cardsCopy[cardIndex] // Return the selected card
                if(validateCard(cardsCopy[cardIndex], tableCards, gameType)){
                    cardsCopy[cardIndex]
                } else {
                    println("Invalid card. Please try again.")
                    playCard(tableCards, gameType) // Retry if the card is invalid
                }
            } else {
                println("Invalid card number. Please try again.")
                playCard(tableCards, gameType) // Retry if the input was invalid
            }
        } catch (e: NumberFormatException) {
            println("Invalid input. Please enter a number.")
            playCard(tableCards, gameType) // Retry on invalid input
        }
    }

}