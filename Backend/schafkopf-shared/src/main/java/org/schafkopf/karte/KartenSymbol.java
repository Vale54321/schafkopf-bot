package org.schafkopf.karte;

/**
 * Enum for all possible Card Symbols.
 */
public enum KartenSymbol {
  SIX("6", "6", 0),
  SEVEN("7", "7", 0),
  EIGHT("8", "8", 0),
  NINE("9", "9", 0),
  UNTER("u", "Unter", 2),
  OBER("o", "Ober", 3),
  KOENIG("k", "König", 4),
  TEN("x", "10", 10),
  ASS("a", "Ass", 11);

  private final String displayName;
  private final String id;
  private final int value;

  KartenSymbol(String id, String displayName, int value) {
    this.displayName = displayName;
    this.value = value;
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public int getValue() {
    return value;
  }

  public String getId() {
    return id;
  }
}
