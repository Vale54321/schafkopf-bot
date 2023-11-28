package org.schafkopf;

import org.schafkopf.GameState.GamePhase;
import org.schafkopf.karte.Karte;
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
      schafkopf.sendGameState(new GameState(GamePhase.ROUND_START));
      logger.info("Stich: {}", gemachteStiche);
      for (int i = 0; i < 4; i++) {
        int nextPlayer = (i + rauskommer) % 4;

        schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());

        logger.info("Spieler ist dran: {}", nextPlayer);
        schafkopf.sendGameState(new GameState(GamePhase.WAIT_FOR_CARD, nextPlayer));

        Karte playedCard = players[nextPlayer].play(spiel, tischKarten, gespielteKarten);
        tischKarten.addKarten(playedCard);

        schafkopf.sendGameState(
            new GameState(
                GamePhase.PLAYER_CARD,
                nextPlayer,
                playedCard,
                tischKarten.getByIndex(0).getFarbe(),
                spiel.isTrumpf(tischKarten.getByIndex(0))));
        schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      }
      int stichSpieler = SpielController.welcheKarteSticht(tischKarten);
      schafkopf
          .getServer()
          .sendMessageToAllFrontendEndpoints(tischKarten.getByIndex(stichSpieler).getJson());

      logger.info("Stiche ende");

      rauskommer = (rauskommer + stichSpieler) % 4;
      logger.warn("Karte sticht: {}", rauskommer);

      schafkopf.sendGameState(
          new GameState(GamePhase.PLAYER_TRICK, rauskommer, tischKarten.getByIndex(stichSpieler)));
      // rauskommer = 0;
      Thread.sleep(3000);
      gespielteKarten.addKarten(tischKarten);

      tischKarten.clear();

      gemachteStiche++;
    }
    schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
    schafkopf.stopGame();
  }
}
