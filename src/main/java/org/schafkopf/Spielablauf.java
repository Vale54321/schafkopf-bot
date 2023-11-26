package org.schafkopf;

import org.schafkopf.karte.KartenListe;
import org.schafkopf.player.Player;
import org.schafkopf.spielcontroller.SpielController;

/** The main class that controlls the game flow. */
public class Spielablauf {
  private final KartenListe gespielteKarten;

  private final KartenListe tischKarten = new KartenListe();

  private final SpielController spiel;

  private final Player[] players;
  private final boolean aktiverSpieler; // ob der bot Spieler ist oder nicht
  private final Schafkopf schafkopf;

  private int gemachteStiche;

  Spielablauf(Schafkopf schafkopf, SpielController spiel, boolean aktiverSpieler) {
    this.schafkopf = schafkopf;
    this.spiel = spiel;
    this.players = schafkopf.getPlayer();
    this.aktiverSpieler = aktiverSpieler;
    gespielteKarten = new KartenListe();
    gemachteStiche = 0;
    try {
      einStich();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /** Method to Handle flow of one Game. */
  public void einStich() throws InterruptedException {
    System.out.println("Starte Stiche");
    int rauskommer = 0;
    while (gemachteStiche < 8) {
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
      System.out.println("Stich: " + gemachteStiche);
      for (int i = 0; i < 4; i++) {
        tischKarten.addKarten(players[(i + rauskommer) % 4].play(spiel, tischKarten));
        schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      }
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      int stichSpieler = SpielController.welcheKarteSticht(tischKarten);
      Thread.sleep(2000);
      System.out.println("Stiche ende");

      //rauskommer = (rauskommer + stichSpieler) % 4;
      rauskommer = 0;

      gespielteKarten.addKarten(tischKarten);

      tischKarten.clear();

      gemachteStiche++;
    }
    schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
    schafkopf.stopGame();
  }
}
