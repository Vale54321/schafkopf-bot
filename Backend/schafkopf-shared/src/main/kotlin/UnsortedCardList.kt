package de.heiserer

interface CardList {
    fun add(card: Card)
    fun add(cards: CardList)
    fun remove(card: Card): Card
    fun remove(cards: CardList)
    fun removeLast(): Card
    fun get(index: Int): Card
    fun getCopyOfCards(): List<Card>
    fun size(): Int
    fun print()
    fun asSortedCardList(type: GameType): SortedCardList
    fun removeFirst(): Card
}

open class UnsortedCardList(withAllCards: Boolean = false): CardList {
    private val cards: MutableList<Card> = if(withAllCards){
        enumValues<Card>().toMutableList()
    } else {
        mutableListOf()
    }

    override fun add(card: Card) {
        if (card !in cards) {
            cards.add(card)
        } else {
            throw CardAlreadyAddedException("Karte $card is already in the deck.", card)
        }
    }

    override fun add(cards: CardList) {
        cards.getCopyOfCards().forEach { card -> add(card) }
    }

    override fun remove(card: Card) : Card {
        if (card !in cards) {
            throw IllegalArgumentException("Karte $card is not in the deck.")
        } else {
            cards.remove(card)
            return card
        }
    }

    override fun remove(cards: CardList) {
        cards.getCopyOfCards().forEach { card -> remove(card) }
    }

    override fun removeFirst(): Card {
        if (cards.isEmpty()) {
            throw IllegalArgumentException("Deck is empty.")
        }
        return cards.removeFirst()
    }

    override fun removeLast(): Card {
        if (cards.isEmpty()) {
            throw IllegalArgumentException("Deck is empty.")
        }
        return cards.removeLast()
    }

    override fun get(index: Int): Card {
        if(index < 0 || index >= cards.size){
            throw IllegalArgumentException("Index $index is out of bounds.")
        }
        return cards[index]
    }

    protected fun get(color: CardColor): CardList {
        val list = UnsortedCardList()
        cards.forEach { card ->
            if (card.color == color) {
                list.add(card)
            }
        }
        return list
    }

    protected fun get(symbol: CardSymbol): CardList {
        val list = UnsortedCardList()
        cards.forEach { card ->
            if (card.symbol == symbol) {
                list.add(card)
            }
        }
        return list
    }

    override fun getCopyOfCards(): List<Card> {
        return cards.toList()
    }

    override fun size(): Int {
        return cards.size
    }

    override fun print() {
        println("KartenDeck:")
        cards.forEach { karte ->
            println(karte.displayName)
        }
        println()
    }

    fun shuffle() {
        cards.shuffle()
    }

    override fun asSortedCardList(type: GameType): SortedCardList {
        val sortedList = SortedCardList(type)
        cards.forEach { card -> sortedList.add(card) }
        return sortedList
    }

    protected fun sortInternal(symbol: CardSymbol? = null, color: CardColor? = null) {
        if (symbol != null && symbol != CardSymbol.OBER && symbol != CardSymbol.UNTER) {
            throw IllegalArgumentException("Symbol $symbol is not accepted. Only OBER and UNTER are allowed.")
        }

        cards.sortWith(compareBy<Card> {
            when  {
                symbol == null && it.symbol == CardSymbol.OBER -> 3
                symbol == null && it.symbol == CardSymbol.UNTER -> 2
                symbol != null && it.symbol == symbol -> 2
                color != null && it.color == color -> 1
                else -> 0
            }
        }.thenComparing(compareBy({ it.color.order }, { it.symbol.order })))
    }
}

class SortedCardList(private val gameType: GameType, withAllCards: Boolean = false) : UnsortedCardList(withAllCards) {
    private fun sort() {
        super.sortInternal(gameType.symbol, gameType.color)
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

    fun getTrumpf():SortedCardList {
        val trumpf = SortedCardList(gameType)

        gameType.symbol?.let {
            trumpf.add(get(it))
        } ?: run {
            trumpf.add(get(CardSymbol.OBER))
            trumpf.add(get(CardSymbol.UNTER))
        }

        gameType.color?.let { try{
            trumpf.add(get(it))
        } catch (_: CardAlreadyAddedException) {}
        }

        return trumpf
    }

    fun farbFreiOrTrumpf(firstCard: Card, gameType: GameType) =
        firstCard.color == gameType.color || getCardsWithoutTrumpf(gameType.color).size() == 0

    override fun add(card: Card) {
        super.add(card)
        sort()
    }
}

class CardAlreadyAddedException(message: String, val card: Card) : RuntimeException(message) {

    // You can add additional constructors or methods if needed, but for now, this is sufficient to handle the scenario of adding a card that's already in the list.
}