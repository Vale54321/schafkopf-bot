package org.schafkopf.spielcontroller;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenSymbol;
import org.schafkopf.karte.KartenUtil;

/**
 * SpielController that implements Logic of a Farb Solo.
 */
public class FarbSoloController extends SoloController {
  /**
   * Create instance of SpielController.
   *
   * @param farbe Trumpffarbe of the Farb Solo.
   */
  public FarbSoloController(int activePlayer, KartenFarbe farbe) {
    super(activePlayer);
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe unterKarten = kartenList.getKarten(KartenSymbol.UNTER);

    KartenListe farbTrumpfKarten = kartenList.getKarten(farbe);
    farbTrumpfKarten.removeKarten(KartenSymbol.UNTER);
    farbTrumpfKarten.removeKarten(KartenSymbol.OBER);
    farbTrumpfKarten.addKarten(kartenList.getKarten(KartenSymbol.UNTER));
    farbTrumpfKarten.addKarten(kartenList.getKarten(KartenSymbol.OBER));

    kartenList.removeKarten(farbTrumpfKarten);

    this.trumpfKarten = new KartenListe(farbTrumpfKarten);
    this.farbKarten = new KartenListe(kartenList);
  }

  public int welcheKarteSpielIch(int meinePosition,
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten) {
    return 0;
  }
}
