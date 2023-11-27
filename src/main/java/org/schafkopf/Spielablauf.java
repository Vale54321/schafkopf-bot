package org.schafkopf;

import org.schafkopf.karte.KartenListe;
import org.schafkopf.player.Player;
import org.schafkopf.spielcontroller.SpielController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The main class that controlls the game flow. */
public class Spielablauf {

  private static final Logger logger = LoggerFactory.getLogger(Spielablauf.class);
  private final KartenListe gespielteKarten;

  private final KartenListe tischKarten = new KartenListe();

  private final SpielController spiel;

  private final Player[] players;

  private final Schafkopf schafkopf;

  private int gemachteStiche;

  Spielablauf(Schafkopf schafkopf, SpielController spiel) {
    this.schafkopf = schafkopf;
    this.spiel = spiel;
    this.players = schafkopf.getPlayer();
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
    logger.info("Starte Stiche");
    int rauskommer = 0;
    while (gemachteStiche < 8) {
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
      logger.info("Stich: {}", gemachteStiche);
      for (int i = 0; i < 4; i++) {
        schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
        int nextPlayer = (i + rauskommer) % 4;

        logger.info("Spieler ist dran: {}", nextPlayer);

        tischKarten.addKarten(players[nextPlayer].play(spiel, tischKarten));
        schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      }
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      int stichSpieler = SpielController.welcheKarteSticht(tischKarten);



      Thread.sleep(2000);
      logger.info("Stiche ende");

      rauskommer = (rauskommer + stichSpieler) % 4;
      logger.warn("Karte sticht: {}", rauskommer);
      //rauskommer = 0;

      gespielteKarten.addKarten(tischKarten);

      tischKarten.clear();

      gemachteStiche++;
    }
    schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
    schafkopf.stopGame();
  }
}
