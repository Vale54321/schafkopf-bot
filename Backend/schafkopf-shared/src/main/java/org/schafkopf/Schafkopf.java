package org.schafkopf;

import org.schafkopf.GameState.GamePhase;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
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
  public void startGame() throws InterruptedException {
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
        if (currentPlayer instanceof OnlinePlayer onlinePlayer) {
          KartenListe karten = new KartenListe();
          for (int i = 7; i >= 0; i--) {
            karten.addKarten(austeilen.removeKarten(austeilen.getByIndex(i)));
          }
          onlinePlayer.setAndSendPlayerCards(karten);
        }
      }

      new Spielablauf(this, spiel);
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
   * Class that represents one Frontend Connection.
   */
  public void setAndSendGameState(GameState gameState) {
    this.gameState = gameState;
    this.messageSender.sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.GAME_STATE, gameState.getJson()));
  }

  public GameState getGameState() {
    return this.gameState;
  }
}
