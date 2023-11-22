package org.example.spielcontroller;

import org.example.karte.KartenFarbe;
import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

/**
 * SpielController that has the standard Card Deck for Sauspiel, Bettel und Co.
 */
public abstract class StandardController extends SpielController {

  StandardController() {
    KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
    KartenListe herzKarten = kartenList.getKarten(KartenFarbe.HERZ);
    herzKarten.removeKarten(KartenSymbol.UNTER);
    herzKarten.removeKarten(KartenSymbol.OBER);

    herzKarten.addKarten(kartenList.getKarten(KartenSymbol.UNTER));
    herzKarten.addKarten(kartenList.getKarten(KartenSymbol.OBER));

    kartenList.removeKarten(herzKarten);

    this.trumpfKarten = new KartenListe(herzKarten);
    this.farbKarten = new KartenListe(kartenList);
  }

  public abstract int welcheKarteSpielIch(
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten);
}
