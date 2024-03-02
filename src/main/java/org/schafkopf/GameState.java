package org.schafkopf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;

public class GameState {

  public GamePhase getGamePhase() {
    return this.gamePhase;
  }

  public enum GamePhase {
    CHOOSE_GAME("Spiel muss gewählt werden"),

    GAME_START("Warten auf das Legen einer Karte"),

    TRICK_START("Warten auf das Legen einer Karte"),
    WAIT_FOR_CARD("Warten auf das Legen einer Karte"),
    PLAYER_CARD("Warten auf das Legen einer Karte"),
    PLAYER_TRICK("Spieler sticht"),
    GAME_STOP("Spieler sticht");
    // Add more phases as needed

    private final String description;

    GamePhase(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }

  private GamePhase gamePhase;
  private Integer currentPlayer; // Using Integer to allow for null
  private Karte card;
  private KartenFarbe color;
  private boolean trumpf;

  // Constructors, getters, and setters

  public GameState(GamePhase phase) {
    this.gamePhase = phase;
  }

  public GameState(GamePhase phase, Integer player) {
    this.gamePhase = phase;
    this.currentPlayer = player;
  }

  public GameState(GamePhase phase, Integer player, Karte card, KartenFarbe color, boolean trumpf) {
    this.gamePhase = phase;
    this.currentPlayer = player;
    this.card = card;
    this.color = color;
    this.trumpf = trumpf;
  }

  public GameState(GamePhase phase, Integer player, Karte card) {
    this.gamePhase = phase;
    this.currentPlayer = player;
    this.card = card;
  }

  public JsonObject getJson() {
    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("gamestate", gson.toJsonTree(this));

    return jsonObject;
  }
}
