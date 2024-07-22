import de.heiserer.cards.Card
import de.heiserer.cards.CardColor
import de.heiserer.cards.GameType
import de.heiserer.cards.UnsortedCardList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class UnsortedListTest {
    @Test
    fun `init with all cards`() {
        assertEquals(32, UnsortedCardList(true).size())
    }
}

class SortingCardsTest {
    private val defaultList = listOf(
        Card.SCHELL_7,
        Card.SCHELL_K,
        Card.BLATT_8,
        Card.BLATT_9,
        Card.EICHEL_A,
        Card.HERZ_X,
        Card.EICHEL_U,
        Card.HERZ_O
    )

    companion object {
        @JvmStatic
        fun gameTypeProvider() = GameType.values().asList()
    }

    private fun testCardSorting(expectedCards: List<Card>, gameType: GameType) {
        // ARRANGE
        var cards = UnsortedCardList()
        defaultList.shuffled().forEach {
            cards.add(it)
        }

        // ACT
        cards = cards.asSortedCardList(gameType)

        // ASSERT
        assertEquals(expectedCards.size, cards.size())
        expectedCards.forEachIndexed { index, card ->
            assertEquals(card, cards.get(index))
        }
    }

    private fun expectedCardsForGameType(gameType: GameType): List<Card> {
        return when(gameType) {
            GameType.RAMSCH -> defaultList
            GameType.SAU_SPIEL -> defaultList
            GameType.BETTEL -> defaultList

            GameType.SCHELL_GEIER -> listOf(
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O
            )
            GameType.HERZ_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.HERZ_X,
                Card.HERZ_O
            )
            GameType.BLATT_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.HERZ_O
            )
            GameType.EICHEL_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.HERZ_O
            )

            GameType.SCHELL_WENZ -> listOf(
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.EICHEL_U,
            )
            GameType.HERZ_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.EICHEL_U
            )
            GameType.BLATT_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.EICHEL_A,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
            )
            GameType.EICHEL_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.EICHEL_U
            )

            GameType.GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.HERZ_O
            )
            GameType.WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.EICHEL_U,
            )

            GameType.SCHELL_SOLO -> listOf(
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.BLATT_SOLO -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.EICHEL_A,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.EICHEL_SOLO -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.HERZ_SOLO -> defaultList
            else -> throw IllegalArgumentException("No expected cards defined for gameType: $gameType")
        }
    }

    @ParameterizedTest(name = "test sorting for {0}") // This gives a clear name in the test output
    @MethodSource("gameTypeProvider")
    fun `test sorting for all game types`(gameType: GameType) {
        // ARRANGE
        val expectedCards = expectedCardsForGameType(gameType)

        // ACT & ASSERT
        testCardSorting(expectedCards, gameType)
    }




    private fun testGetTrumpfCard(expectedCards: List<Card>, gameType: GameType) {
        // ARRANGE
        var cards = UnsortedCardList()
        defaultList.shuffled().forEach {
            cards.add(it)
        }

        // ACT
        cards = cards.asSortedCardList(gameType).getTrumpf()

        // ASSERT
        expectedCards.forEachIndexed { index, card ->
            assertEquals(card, cards.get(index))
        }
    }

    private fun expectedTrumpfCardsForGameType(gameType: GameType): List<Card> {
        val herzList = listOf(
            Card.HERZ_X,
            Card.EICHEL_U,
            Card.HERZ_O
        )
        return when(gameType) {
            GameType.RAMSCH -> herzList
            GameType.SAU_SPIEL -> herzList
            GameType.BETTEL -> herzList

            GameType.SCHELL_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O
            )
            GameType.HERZ_GEIER -> listOf(
                Card.HERZ_X,
                Card.HERZ_O
            )
            GameType.BLATT_GEIER -> listOf(
                Card.BLATT_8,
                Card.BLATT_9,
                Card.HERZ_O
            )
            GameType.EICHEL_GEIER -> listOf(
                Card.EICHEL_U,
                Card.EICHEL_A,
                Card.HERZ_O
            )

            GameType.SCHELL_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.EICHEL_U,
            )
            GameType.HERZ_WENZ -> listOf(
                Card.HERZ_O,
                Card.HERZ_X,
                Card.EICHEL_U
            )
            GameType.BLATT_WENZ -> listOf(
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
            )
            GameType.EICHEL_WENZ -> listOf(
                Card.EICHEL_A,
                Card.EICHEL_U
            )

            GameType.GEIER -> listOf(
                Card.HERZ_O
            )
            GameType.WENZ -> listOf(
                Card.EICHEL_U,
            )

            GameType.SCHELL_SOLO -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.BLATT_SOLO -> listOf(
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.EICHEL_SOLO -> listOf(
                Card.EICHEL_A,
                Card.EICHEL_U,
                Card.HERZ_O
            )
            GameType.HERZ_SOLO -> herzList
            else -> throw IllegalArgumentException("No expected cards defined for gameType: $gameType")
        }
    }

    @ParameterizedTest(name = "test get Trumpfcards for {0}") // This gives a clear name in the test output
    @MethodSource("gameTypeProvider")
    fun `test get Trumpfcards for all game types`(gameType: GameType) {
        // ARRANGE
        val expectedCards = expectedTrumpfCardsForGameType(gameType)

        // ACT & ASSERT
        testGetTrumpfCard(expectedCards, gameType)
    }



    private fun testCardsWithoutTrumpf(expectedCards: List<Card>, gameType: GameType) {
        // ARRANGE
        var cards = UnsortedCardList()
        defaultList.shuffled().forEach {
            cards.add(it)
        }

        // ACT
        cards = cards.asSortedCardList(gameType).getCardsWithoutTrumpf()

        // ASSERT
        expectedCards.forEachIndexed { index, card ->
            assertEquals(card, cards.get(index))
        }
    }

    private fun expectedCardsWithoutTrumpfForGameType(gameType: GameType): List<Card> {
        val herzList = listOf(
            Card.SCHELL_7,
            Card.SCHELL_K,
            Card.BLATT_8,
            Card.BLATT_9,
            Card.EICHEL_A,
        )
        return when(gameType) {
            GameType.RAMSCH -> herzList
            GameType.SAU_SPIEL -> herzList
            GameType.BETTEL -> herzList

            GameType.SCHELL_GEIER -> listOf(
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
            )
            GameType.HERZ_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
            )
            GameType.BLATT_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.EICHEL_U,
                Card.EICHEL_A,
            )
            GameType.EICHEL_GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
            )

            GameType.SCHELL_WENZ -> listOf(
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
            )
            GameType.HERZ_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
            )
            GameType.BLATT_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.EICHEL_A,
            )
            GameType.EICHEL_WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
            )

            GameType.GEIER -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_U,
                Card.EICHEL_A,
            )
            GameType.WENZ -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_O,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
            )

            GameType.SCHELL_SOLO -> listOf(
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
                Card.EICHEL_A,
            )
            GameType.BLATT_SOLO -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.EICHEL_A,
            )
            GameType.EICHEL_SOLO -> listOf(
                Card.SCHELL_7,
                Card.SCHELL_K,
                Card.HERZ_X,
                Card.BLATT_8,
                Card.BLATT_9,
            )
            GameType.HERZ_SOLO -> herzList
            else -> throw IllegalArgumentException("No expected cards defined for gameType: $gameType")
        }
    }

    @ParameterizedTest(name = "test get Cards without Trumpf for {0}") // This gives a clear name in the test output
    @MethodSource("gameTypeProvider")
    fun `test get Cards without Trumpf for all game types`(gameType: GameType) {
        // ARRANGE
        val expectedCards = expectedCardsWithoutTrumpfForGameType(gameType)

        // ACT & ASSERT
        testCardsWithoutTrumpf(expectedCards, gameType)
    }

    @Test
    fun `test get Cards without Trumpf with Color`() {
        // ARRANGE
        var cards = UnsortedCardList()
        defaultList.shuffled().forEach {
            cards.add(it)
        }

        val expectedCards = listOf(
            Card.BLATT_8,
            Card.BLATT_9,
        )

        // ACT
        cards = cards.asSortedCardList(GameType.SAU_SPIEL).getCardsWithoutTrumpf(CardColor.BLATT)

        // ASSERT
        expectedCards.forEachIndexed { index, card ->
            assertEquals(card, cards.get(index))
        }
    }
}