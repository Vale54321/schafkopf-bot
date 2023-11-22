package org.example.spielcontroller;

import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Geier Game.
 */
public class GeierController extends GeierWenzController {
  /**
   * Create instance of Geier Game.
   */
  public GeierController() {
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe oberKarten = kartenList.getKarten(KartenSymbol.OBER);

    kartenList.removeKarten(oberKarten);

    this.trumpfKarten = new KartenListe(oberKarten);
    this.farbKarten = new KartenListe(kartenList);
  }
}
