package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Geier Game.
 */
public class GeierController extends GeierWenzController {
  /**
   * Create instance of Geier Game.
   */
  public GeierController(int activePlayer) {
    super(activePlayer);
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe oberKarten = kartenList.getKarten(KartenSymbol.OBER);

    kartenList.removeKarten(oberKarten);

    this.trumpfKarten = new KartenListe(oberKarten);
    this.farbKarten = new KartenListe(kartenList);
  }
}
