package org.example.karte;

public enum KartenSymbol {
    SIX("6", 0),
    SEVEN("7", 0),
    EIGHT("8", 0),
    NINE("9", 0),
    UNTER("U", 2),
    OBER("O", 3),
    KOENIG("K", 4),
    TEN("X", 10),
    ASS("A", 11);

    private final String displayName;
    private final int value;

    KartenSymbol(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getValue() {
        return value;
    }
}
