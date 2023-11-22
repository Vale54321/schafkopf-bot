package org.example.karte;

public enum KartenFarbe {
  EICHEL("Eichel"),
  BLATT("Blatt"),
  HERZ("Herz"),
  SCHELL("Schell");

  private final String displayName;

  KartenFarbe(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
