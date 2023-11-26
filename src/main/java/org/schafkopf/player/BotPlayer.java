package org.schafkopf.player;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.spielcontroller.SauSpielController;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that represents the Bot.
 */
public class BotPlayer extends Player {

  private final KartenListe eigeneKarten;
  private final KartenListe unbekannteKarten = KartenUtil.initializeSchafKopfCardDeck();

  public BotPlayer(KartenListe hand) {
    this.eigeneKarten = hand;
    this.unbekannteKarten.removeKarten(this.eigeneKarten);
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten) {

    System.out.println("Eigene Karte legen");
    return     eigeneKarten.getByIndex(spiel.welcheKarteSpielIch(1,
        eigeneKarten,
        eigeneKarten,
        tischKarten));
  }
}
