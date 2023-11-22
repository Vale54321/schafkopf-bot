package org.schafkopf.player;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.spielcontroller.SpielController;

public class BotPlayer extends Player {

  private final KartenListe eigeneKarten;
  private final KartenListe unbekannteKarten = new KartenListe();
  public BotPlayer(KartenListe hand) {
    this.eigeneKarten = hand;
    this.unbekannteKarten.removeKarten(this.eigeneKarten);
  }
  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten) {
    spiel.welcheKarteSpielIch(1,
        eigeneKarten,
        eigeneKarten,
        tischKarten);
    return new Karte(KartenFarbe.SCHELL, KartenSymbol.SEVEN);
  }
}
