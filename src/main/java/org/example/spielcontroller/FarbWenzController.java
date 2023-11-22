package org.example.spielcontroller;

import org.example.karte.Karte;
import org.example.karte.KartenFarbe;
import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Farb Wenz.
 */
public class FarbWenzController extends SoloController {
  /**
   * Create instance of SpielController.
   *
   * @param farbe Trumpffarbe of the Farb Wenz.
   */
  public FarbWenzController(KartenFarbe farbe) {
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe unterKarten = kartenList.getKarten(KartenSymbol.UNTER);
    KartenListe farbTrumpfKarten = kartenList.getKarten(farbe);
    farbTrumpfKarten.removeKarten(KartenSymbol.UNTER);
    farbTrumpfKarten.addKarten(unterKarten);
    kartenList.removeKarten(farbTrumpfKarten);

    this.trumpfKarten = new KartenListe(farbTrumpfKarten);
    this.farbKarten = new KartenListe(kartenList);
  }

  public Karte welcheKarteSpielich(
      KartenListe gespielteKarten,
      KartenListe meineHand,
      KartenListe tischKarten,
      boolean istSpieler) {
    return null;
  }
}
