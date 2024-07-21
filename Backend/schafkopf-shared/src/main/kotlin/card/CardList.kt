package de.heiserer.cards

interface CardList {
    fun add(card: Card)
    fun add(cards: CardList)

    fun remove(card: Card): Card
    fun remove(cards: CardList)
    fun removeLast(): Card
    fun removeFirst(): Card

    fun get(index: Int): Card
    fun getLast(): Card
    fun getCopyOfCards(): List<Card>

    fun indexOf(card: Card): Int
    operator fun contains(card: Card): Boolean
    fun size(): Int
    fun print()
    fun asSortedCardList(type: GameType): SortedCardList
}