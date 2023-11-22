package org.example.spielcontroller;

import org.example.karte.KartenFarbe;
import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

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
