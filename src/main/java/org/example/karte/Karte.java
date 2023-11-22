package org.example.karte;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class that represents one Card of the game.
 */
public class Karte {
  private String id;
  private String name;
  private KartenFarbe farbe;
  private KartenSymbol symbol;
  private int punkte;

  Karte(KartenFarbe farbe, KartenSymbol symbol) {
    this.id = farbe.getDisplayName().toLowerCase() + "_" + symbol.getId().toLowerCase();
    this.name = farbe.getDisplayName() + " " + symbol.getDisplayName();
    this.farbe = farbe;
    this.symbol = symbol;
    this.punkte = symbol.getValue();
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
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

  /**
   * get the Card as a Json Object.
   */
  public JsonObject getJson() {
    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("card", gson.toJsonTree(this));

    return jsonObject;
  }
}
