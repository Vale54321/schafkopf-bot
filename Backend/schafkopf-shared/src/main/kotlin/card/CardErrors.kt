package de.heiserer.cards

class CardAlreadyAddedException(message: String, val card: Card) : RuntimeException(message) {

    // You can add additional constructors or methods if needed, but for now, this is sufficient to handle the scenario of adding a card that's already in the list.
}