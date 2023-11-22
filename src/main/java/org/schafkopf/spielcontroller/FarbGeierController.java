package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Farb Geier.
 */
public class FarbGeierController extends SoloController {
  /**
   * Create instance of SpielController.
   *
   * @param farbe Trumpffarbe of the Farb Geier.
   */
  public FarbGeierController(KartenFarbe farbe) {
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe oberKarten = kartenList.getKarten(KartenSymbol.OBER);
    KartenListe farbTrumpfKarten = kartenList.getKarten(farbe);
    farbTrumpfKarten.removeKarten(KartenSymbol.OBER);
    farbTrumpfKarten.addKarten(oberKarten);
    kartenList.removeKarten(farbTrumpfKarten);

    this.trumpfKarten = new KartenListe(farbTrumpfKarten);
    this.farbKarten = new KartenListe(kartenList);
  }
}
