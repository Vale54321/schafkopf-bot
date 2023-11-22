package org.example.spielController;

import org.example.karte.*;

public class FarbSoloController extends SoloController {
  public FarbSoloController(KartenFarbe farbe) {
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

  public Karte welcheKarteSpielich(
      KartenListe gespielteKarten,
      KartenListe meineHand,
      KartenListe tischKarten,
      boolean istSpieler) {
    return null;
  }
}
