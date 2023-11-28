package org.schafkopf.karte;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public enum Karte {
  EICHEL_7(KartenFarbe.EICHEL, KartenSymbol.SEVEN),
  EICHEL_8(KartenFarbe.EICHEL, KartenSymbol.EIGHT),
  EICHEL_9(KartenFarbe.EICHEL, KartenSymbol.NINE),
  EICHEL_X(KartenFarbe.EICHEL, KartenSymbol.TEN),
  EICHEL_K(KartenFarbe.EICHEL, KartenSymbol.KOENIG),
  EICHEL_A(KartenFarbe.EICHEL, KartenSymbol.ASS),
  EICHEL_U(KartenFarbe.EICHEL, KartenSymbol.UNTER),
  EICHEL_O(KartenFarbe.EICHEL, KartenSymbol.OBER),
  BLATT_7(KartenFarbe.BLATT, KartenSymbol.SEVEN),
  BLATT_8(KartenFarbe.BLATT, KartenSymbol.EIGHT),
  BLATT_9(KartenFarbe.BLATT, KartenSymbol.NINE),
  BLATT_X(KartenFarbe.BLATT, KartenSymbol.TEN),
  BLATT_K(KartenFarbe.BLATT, KartenSymbol.KOENIG),
  BLATT_A(KartenFarbe.BLATT, KartenSymbol.ASS),
  BLATT_U(KartenFarbe.BLATT, KartenSymbol.UNTER),
  BLATT_O(KartenFarbe.BLATT, KartenSymbol.OBER),
  SCHELL_7(KartenFarbe.SCHELL, KartenSymbol.SEVEN),
  SCHELL_8(KartenFarbe.SCHELL, KartenSymbol.EIGHT),
  SCHELL_9(KartenFarbe.SCHELL, KartenSymbol.NINE),
  SCHELL_X(KartenFarbe.SCHELL, KartenSymbol.TEN),
  SCHELL_K(KartenFarbe.SCHELL, KartenSymbol.KOENIG),
  SCHELL_A(KartenFarbe.SCHELL, KartenSymbol.ASS),
  SCHELL_U(KartenFarbe.SCHELL, KartenSymbol.UNTER),
  SCHELL_O(KartenFarbe.SCHELL, KartenSymbol.OBER),
  HERZ_7(KartenFarbe.HERZ, KartenSymbol.SEVEN),
  HERZ_8(KartenFarbe.HERZ, KartenSymbol.EIGHT),
  HERZ_9(KartenFarbe.HERZ, KartenSymbol.NINE),
  HERZ_X(KartenFarbe.HERZ, KartenSymbol.TEN),
  HERZ_K(KartenFarbe.HERZ, KartenSymbol.KOENIG),
  HERZ_A(KartenFarbe.HERZ, KartenSymbol.ASS),
  HERZ_U(KartenFarbe.HERZ, KartenSymbol.UNTER),
  HERZ_O(KartenFarbe.HERZ, KartenSymbol.OBER);

  private final String id;
  private final KartenFarbe farbe;
  private final KartenSymbol symbol;

  private final String displayName;

  private final int punkte;

  Karte(KartenFarbe farbe, KartenSymbol symbol) {
    this.farbe = farbe;
    this.symbol = symbol;
    this.id = this.name().toLowerCase();
    this.displayName = farbe.getDisplayName() + " " + symbol.getDisplayName();
    this.punkte = symbol.getValue();
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.displayName;
  }

  public KartenFarbe getFarbe() {
    return this.farbe;
  }

  public KartenSymbol getSymbol() {
    return this.symbol;
  }

  public int getPunkte() {
    return this.punkte;
  }

  /** get the Card as a Json Object. */
  public JsonObject getJson() {
    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("card", gson.toJsonTree(this));

    return jsonObject;
  }
}
