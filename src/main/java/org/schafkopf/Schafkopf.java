package org.schafkopf;

import java.lang.constant.PackageDesc;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.LocalPlayer;
import org.schafkopf.player.Player;
import org.schafkopf.spielcontroller.FarbGeierController;
import org.schafkopf.spielcontroller.FarbSoloController;
import org.schafkopf.spielcontroller.FarbWenzController;
import org.schafkopf.spielcontroller.GeierController;
import org.schafkopf.spielcontroller.SauSpielController;
import org.schafkopf.spielcontroller.SpielController;
import org.schafkopf.spielcontroller.WenzController;

/** The main class representing the Schafkopf game. */
public class Schafkopf {
  private final BackendServer server;

  /** The game controller. This is the class that implements the game logic. */
  private SpielController spiel = new SauSpielController(0, KartenFarbe.EICHEL);

  private final Player[] player = {
      new BotPlayer(KartenUtil.zieheZufallsHand(8)),
      new LocalPlayer(this),
      new LocalPlayer(this),
      new LocalPlayer(this)
  };

  private boolean gameState = false;
  private Thread spielThread;

  public Player[] getPlayer() {
    return player;
  }

  /**
   * Constructor for the Schafkopf class.
   *
   * @param server The backend server associated with the game.
   */
  Schafkopf(BackendServer server) {
    this.server = server;
    System.out.println("SchaffKopfGame erstellt");
  }

  /**
   * Sends all Trumpf Karten of the current GameType to the Frontend.
   */
  public void showTrumpf() {
    server.sendMessageToAllFrontendEndpoints(spiel.getTrumpfKarten().getJson());
  }

  /**
   * Sends all Farb Karten of the current GameType to the Frontend.
   */
  public void showFarbe() {
    server.sendMessageToAllFrontendEndpoints(spiel.getFarbKarten().getJson());
  }

  /**
   * Test to wait for one Card Input via NFC.
   */
  public void testHand() {
    wartetAufKarte().getJson();
  }

  /**
   * Waits for a Card and returns a Karte Object.
   */
  public Karte wartetAufKarte() {
    String uid = null;
    System.out.println("Starte Warten auf Karte");
    try {
      uid = server.waitForCardScan();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    String kartenId = KartenUtil.getIdOfUid(uid);

    if (kartenId == null) {
      System.out.println("Ungültige Karte");
      return wartetAufKarte();
    }
    Karte karte = KartenUtil.getKarteById(kartenId);
    server.sendMessageToAllFrontendEndpoints(karte.getJson());
    System.out.println("Karte gescannt: " + karte.getName());
    System.out.println("Beende Warten auf Karte");
    return KartenUtil.getKarteById(kartenId);
  }

  /**
   * Set GameState to "started" and start Game Thread.
   */
  public void startGame() {
    if (gameState) {
      System.out.println("Game already started!");
      server.sendMessageToAllFrontendEndpoints("Game already started!");
    } else {
      gameState = true;
      System.out.println("Start Game");
      server.sendMessageToAllFrontendEndpoints("Start Game");
      spielThread =
          new Thread(() -> new Spielablauf(this, spiel,  true));

      spielThread.start();
    }
  }

  /**
   * Set GameState to "stopped" and interrupt Game Thread.
   */
  public void stopGame() {
    if (!gameState) {
      System.out.println("no active Game!");
      server.sendMessageToAllFrontendEndpoints("no active Game!");
    } else {
      gameState = false;
      System.out.println("Stop Game");
      server.sendMessageToAllFrontendEndpoints("Stop Game");
    }

    spielThread.interrupt();
  }

  public BackendServer getServer() {
    return this.server;
  }
}
