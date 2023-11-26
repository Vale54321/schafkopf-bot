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

  private KartenListe eigeneKarten;
  private KartenListe unbekannteKarten = KartenUtil.initializeSchafKopfCardDeck();

  public BotPlayer() {
    // TODO document why this constructor is empty
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten) {

    Karte cardIndex = eigeneKarten.getByIndex(spiel.welcheKarteSpielIch(1,
        eigeneKarten,
        eigeneKarten,
        tischKarten));

    eigeneKarten.removeKarten(cardIndex);

    System.out.println("Eigene Karte legen");
    return cardIndex;
  }

  /**
   * Set the Cards of the Player.
   */
  public void setCards(KartenListe cards) {
    System.out.println("Eigene Karte setzen");
    this.eigeneKarten = cards;
    this.unbekannteKarten = KartenUtil.initializeSchafKopfCardDeck();
    this.unbekannteKarten.removeKarten(eigeneKarten);
    System.out.println("Eigene Karte fertig");
  }
}
