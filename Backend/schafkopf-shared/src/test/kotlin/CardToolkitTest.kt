import de.heiserer.cards.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CardToolkitTest {
    @Test
    fun `test isTrumpf`() {
        // ASSERT AND ACT
        assertEquals(true, CardToolkit.isTrumpf(GameType.SAU_SPIEL, Card.HERZ_O))
        assertEquals(false, CardToolkit.isTrumpf(GameType.SAU_SPIEL, Card.SCHELL_7))
    }

    @Test
    fun whoTricks() {
        // ARRANGE
        val cards = UnsortedCardList()
        cards.add(Card.SCHELL_7)
        cards.add(Card.SCHELL_8)
        cards.add(Card.SCHELL_K)
        cards.add(Card.SCHELL_O)

        // ASSERT AND ACT
        assertEquals(3, CardToolkit.whoTricks(GameType.SAU_SPIEL, cards))
    }
}
