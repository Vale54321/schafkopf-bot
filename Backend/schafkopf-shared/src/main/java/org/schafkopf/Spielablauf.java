package org.schafkopf;

import org.schafkopf.GameState.GamePhase;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.player.Player;
import org.schafkopf.spielcontroller.SpielController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class that controlls the game flow.
 */
public class Spielablauf {

  private static final Logger logger = LoggerFactory.getLogger(Spielablauf.class);
  private final KartenListe gespielteKarten = new KartenListe();

  private final KartenListe tischKarten = new KartenListe();

  private final SpielController spiel;

  private final Player[] players;

  private final Schafkopf schafkopf;

  Spielablauf(Schafkopf schafkopf, SpielController spiel) throws InterruptedException {
    this.schafkopf = schafkopf;
    this.spiel = spiel;
    this.players = schafkopf.getPlayer();

    playRound();
  }

  private void playRound() throws InterruptedException {
    int startingPlayer = 0;

    logger.info("Starte Stiche");
    for (int i = 0; i < 8; i++) {
      logger.info("Stich: {}", i);
      startingPlayer = playTrick(startingPlayer);
    }
    schafkopf.stopGame();
  }

  private int playTrick(int startingPlayer) throws InterruptedException {
    schafkopf.setAndSendGameState(new GameState(GamePhase.TRICK_START));

    for (int i = 0; i < 4; i++) {
      int currentPlayer = (i + startingPlayer) % 4;

      logger.info("Spieler ist dran: {}", players[currentPlayer].getName());
      schafkopf.setAndSendGameState(
          new GameState(GamePhase.WAIT_FOR_CARD, players[currentPlayer].getName()));

      Karte playedCard = players[currentPlayer].play(spiel, tischKarten, gespielteKarten);
      tischKarten.addKarten(playedCard);

      schafkopf.setAndSendGameState(
          new GameState(
              GamePhase.PLAYER_CARD,
              players[currentPlayer].getName(),
              playedCard,
              tischKarten.getByIndex(0).getFarbe(),
              spiel.isTrumpf(tischKarten.getByIndex(0))));
    }
    int stichSpieler = SpielController.welcheKarteSticht(tischKarten);

    logger.info("Stiche ende");

    int winningPlayerIndex = (startingPlayer + stichSpieler) % 4;
    logger.warn("Karte sticht: {}", winningPlayerIndex);

    schafkopf.setAndSendGameState(
        new GameState(
            GamePhase.PLAYER_TRICK, players[winningPlayerIndex].getName(),
            tischKarten.getByIndex(stichSpieler)));

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      logger.error("error sleep");
    }

    gespielteKarten.addKarten(tischKarten);
    tischKarten.clear();

    return winningPlayerIndex;
  }
}
