package org.example.spielcontroller;

import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Wenz Game.
 */
public class WenzController extends GeierWenzController {
  /**
   * Create instance of Wenz Game.
   */
  public WenzController() {
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe unterKarten = kartenList.getKarten(KartenSymbol.UNTER);

    kartenList.removeKarten(unterKarten);

    this.trumpfKarten = new KartenListe(unterKarten);
    this.farbKarten = new KartenListe(kartenList);
  }
}
