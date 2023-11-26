package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Wenz Game.
 */
public class WenzController extends GeierWenzController {
  /**
   * Create instance of Wenz Game.
   */
  public WenzController(int activePlayer) {
    super(activePlayer);
    this.activePlayer = activePlayer;
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe unterKarten = kartenList.getKarten(KartenSymbol.UNTER);

    kartenList.removeKarten(unterKarten);

    this.trumpfKarten = new KartenListe(unterKarten);
    this.farbKarten = new KartenListe(kartenList);
  }
}
