package de.heiserer

enum class Card(val color: CardColor, val symbol: CardSymbol) {
    SCHELL_7(CardColor.SCHELL, CardSymbol.SIEBEN),
    SCHELL_8(CardColor.SCHELL, CardSymbol.ACHT),
    SCHELL_9(CardColor.SCHELL, CardSymbol.NINE),
    SCHELL_U(CardColor.SCHELL, CardSymbol.UNTER),
    SCHELL_O(CardColor.SCHELL, CardSymbol.OBER),
    SCHELL_K(CardColor.SCHELL, CardSymbol.KOENIG),
    SCHELL_X(CardColor.SCHELL, CardSymbol.ZEHN),
    SCHELL_A(CardColor.SCHELL, CardSymbol.ASS),
    HERZ_7(CardColor.HERZ, CardSymbol.SIEBEN),
    HERZ_8(CardColor.HERZ, CardSymbol.ACHT),
    HERZ_9(CardColor.HERZ, CardSymbol.NINE),
    HERZ_U(CardColor.HERZ, CardSymbol.UNTER),
    HERZ_O(CardColor.HERZ, CardSymbol.OBER),
    HERZ_K(CardColor.HERZ, CardSymbol.KOENIG),
    HERZ_X(CardColor.HERZ, CardSymbol.ZEHN),
    HERZ_A(CardColor.HERZ, CardSymbol.ASS),
    BLATT_7(CardColor.BLATT, CardSymbol.SIEBEN),
    BLATT_8(CardColor.BLATT, CardSymbol.ACHT),
    BLATT_9(CardColor.BLATT, CardSymbol.NINE),
    BLATT_U(CardColor.BLATT, CardSymbol.UNTER),
    BLATT_O(CardColor.BLATT, CardSymbol.OBER),
    BLATT_K(CardColor.BLATT, CardSymbol.KOENIG),
    BLATT_X(CardColor.BLATT, CardSymbol.ZEHN),
    BLATT_A(CardColor.BLATT, CardSymbol.ASS),
    EICHEL_7(CardColor.EICHEL, CardSymbol.SIEBEN),
    EICHEL_8(CardColor.EICHEL, CardSymbol.ACHT),
    EICHEL_9(CardColor.EICHEL, CardSymbol.NINE),
    EICHEL_U(CardColor.EICHEL, CardSymbol.UNTER),
    EICHEL_O(CardColor.EICHEL, CardSymbol.OBER),
    EICHEL_K(CardColor.EICHEL, CardSymbol.KOENIG),
    EICHEL_X(CardColor.EICHEL, CardSymbol.ZEHN),
    EICHEL_A(CardColor.EICHEL, CardSymbol.ASS);

    val id = name.lowercase()
    val displayName = "${color.displayName} ${symbol.displayName}"
    val points = symbol.value
}

enum class CardColor(val order: Int, val displayName: String) {
    SCHELL(0,"Schell"),
    HERZ(1,"Herz"),
    BLATT(2,"Blatt"),
    EICHEL(3,"Eichel")
}

enum class CardSymbol(val order: Int, val displayName: String, val value: Int) {
    SIEBEN(0,"7", 0),
    ACHT(1,"8", 0),
    NINE(2,"9", 0),
    UNTER(3,"Unter", 2),
    OBER(4,"Ober", 3),
    KOENIG(5,"König", 4),
    ZEHN(6,"10", 10),
    ASS(7,"Ass", 11)
}

enum class GameType(val color: CardColor?, val symbol: CardSymbol?){
    RAMSCH(CardColor.HERZ, null),
    SAU_SPIEL(CardColor.HERZ, null),
    BETTEL(CardColor.HERZ, null),

    SCHELL_GEIER(CardColor.SCHELL, CardSymbol.OBER),
    HERZ_GEIER(CardColor.HERZ, CardSymbol.OBER),
    BLATT_GEIER(CardColor.BLATT, CardSymbol.OBER),
    EICHEL_GEIER(CardColor.EICHEL, CardSymbol.OBER),

    SCHELL_WENZ(CardColor.SCHELL, CardSymbol.UNTER),
    HERZ_WENZ(CardColor.HERZ, CardSymbol.UNTER),
    BLATT_WENZ(CardColor.BLATT, CardSymbol.UNTER),
    EICHEL_WENZ(CardColor.EICHEL, CardSymbol.UNTER),

    GEIER(null, CardSymbol.OBER),
    WENZ(null, CardSymbol.UNTER),

    SCHELL_SOLO(CardColor.SCHELL, null),
    BLATT_SOLO(CardColor.BLATT, null),
    EICHEL_SOLO(CardColor.EICHEL, null),
    HERZ_SOLO(CardColor.HERZ, null),
}