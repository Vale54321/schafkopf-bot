package de.heiserer.cards

open class UnsortedCardList(withAllCards: Boolean = false): CardList {
    private val cards: MutableList<Card> = if(withAllCards){
        enumValues<Card>().toMutableList()
    } else {
        mutableListOf()
    }

    override operator fun contains(card: Card): Boolean {
        return cards.contains(card)
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

    override fun indexOf(card: Card): Int {
        return cards.indexOf(card)
    }

    override fun get(index: Int): Card {
        if(index < 0 || index >= cards.size){
            throw IllegalArgumentException("Index $index is out of bounds.")
        }
        return cards[index]
    }

    override fun getLast(): Card {
        return cards[cards.size - 1]
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
