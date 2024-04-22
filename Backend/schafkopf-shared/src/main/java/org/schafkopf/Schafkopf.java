package org.schafkopf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.schafkopf.GameState.GamePhase;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.OnlinePlayer;
import org.schafkopf.player.Player;
import org.schafkopf.spielcontroller.SauSpielController;
import org.schafkopf.spielcontroller.SpielController;

/**
 * The main class representing the Schafkopf game.
 */
public class Schafkopf {

  private final MessageSender messageSender;

  /**
   * The game controller. This is the class that implements the game logic.
   */
  private SpielController spiel = new SauSpielController(0, KartenFarbe.EICHEL);

  private final Player[] player;

  private GameState gameState = new GameState(GamePhase.GAME_STOP);
  private Thread spielThread;

  /**
   * Constructor for the Schafkopf class.
   *
   * @param messageSender MessageSender
   */
  public Schafkopf(Player[] player, MessageSender messageSender) throws NotEnoughPlayersException {
    this.player = player;

    if (player.length < 4) {
      throw new NotEnoughPlayersException();
    }

    this.messageSender = messageSender;
    System.out.println("SchaffKopfGame erstellt");
  }

  public Player[] getPlayer() {
    return player;
  }

  /**
   * Set GameState to "started" and start Game Thread.
   */
  public void startGame() {
    if (gameState.getGamePhase() != GamePhase.GAME_STOP) {
      System.out.println("Game already started!");
      messageSender.sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR));
    } else {
      gameState = new GameState(GamePhase.GAME_START);
      setAndSendGameState(gameState);
      System.out.println("Start Game");

      KartenListe austeilen = KartenUtil.initializeSchafKopfCardDeck();
      austeilen.shuffle();
      for (Player currentPlayer : player) {
        if (currentPlayer instanceof BotPlayer botPlayer) {
          KartenListe botHand = new KartenListe();
          for (int i = 7; i >= 0; i--) {
            botHand.addKarten(austeilen.removeKarten(austeilen.getByIndex(i)));
          }
          System.out.println("Bot Hand: " + botHand.getJson().toString());
          botPlayer.setCards(botHand); // Replace with the actual method you want to call
        }
      }

      for (Player currentPlayer : player) {
        if (currentPlayer instanceof OnlinePlayer) {
          Karte[] karten = new Karte[8];
          for (int i = 7; i >= 0; i--) {
            karten[i] = austeilen.removeKarten(austeilen.getByIndex(i));
          }
          Gson gson = new Gson();
          JsonObject messageObject = new JsonObject();
          messageObject.add("cards", gson.toJsonTree(karten));

          messageSender.sendMessage(
              new SchafkopfBaseMessage(SchafkopfMessageType.ONLINE_PLAYER_HAND, messageObject));
        }
      }

      spielThread = new Thread(() -> new Spielablauf(this, spiel));

      spielThread.start();
    }
  }

  /**
   * Set GameState to "stopped" and interrupt Game Thread.
   */
  public void stopGame() {
    if (gameState.getGamePhase() == GamePhase.GAME_STOP) {
      System.out.println("no active Game!");
      messageSender.sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR));
    } else {
      gameState = new GameState(GamePhase.GAME_STOP);
      setAndSendGameState(gameState);
    }

    spielThread.interrupt();
  }

  /**
   * Set GameType.
   */
//  public void setGame(String message) {
//    System.out.println("Set Game: " + message);
//    messageSender.sendMessage("Set Game: " + message);
//    switch (message) {
//      case "setgame:herzsolo":
//        this.spiel = new FarbSoloController(0, KartenFarbe.HERZ);
//        break;
//      case "setgame:blattsolo":
//        this.spiel = new FarbSoloController(0, KartenFarbe.BLATT);
//        break;
//      case "setgame:eichelsolo":
//        this.spiel = new FarbSoloController(0, KartenFarbe.EICHEL);
//        break;
//      case "setgame:schellsolo":
//        this.spiel = new FarbSoloController(0, KartenFarbe.SCHELL);
//        break;
//
//      case "setgame:wenz":
//        this.spiel = new WenzController(0);
//        break;
//      case "setgame:geier":
//        this.spiel = new GeierController(0);
//        break;
//
//      case "setgame:eichelwenz":
//        this.spiel = new FarbWenzController(0, KartenFarbe.EICHEL);
//        break;
//      case "setgame:herzwenz":
//        this.spiel = new FarbWenzController(0, KartenFarbe.HERZ);
//        break;
//      case "setgame:blattwenz":
//        this.spiel = new FarbWenzController(0, KartenFarbe.BLATT);
//        break;
//      case "setgame:schellwenz":
//        this.spiel = new FarbWenzController(0, KartenFarbe.SCHELL);
//        break;
//
//      case "setgame:eichelgeier":
//        this.spiel = new FarbGeierController(0, KartenFarbe.EICHEL);
//        break;
//      case "setgame:herzgeier":
//        this.spiel = new FarbGeierController(0, KartenFarbe.HERZ);
//        break;
//      case "setgame:blattgeier":
//        this.spiel = new FarbGeierController(0, KartenFarbe.BLATT);
//        break;
//      case "setgame:schellgeier":
//        this.spiel = new FarbGeierController(0, KartenFarbe.SCHELL);
//        break;
//
//      case "setgame:sauspiel":
//        this.spiel = new SauSpielController(0, KartenFarbe.EICHEL);
//        break;
//      default:
//        System.out.println("Ungültiges Spiel");
//    }
//  }
  public void setAndSendGameState(GameState gameState) {
    this.gameState = gameState;
    this.messageSender.sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.GAME_STATE, gameState.getJson()));
  }

  public GameState getGameState() {
    return this.gameState;
  }
}
