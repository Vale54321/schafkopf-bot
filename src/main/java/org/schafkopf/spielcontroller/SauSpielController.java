package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;

/**
 * SpielController that implements Logic of a Sau Spiel Game.
 */
public class SauSpielController extends StandardController {

  KartenFarbe suchFarbe;
  boolean istSpieler;

  public SauSpielController(int activePlayer, KartenFarbe farbe) {
    super(activePlayer);
    this.suchFarbe = suchFarbe;
    this.istSpieler = istSpieler;
  }

  /**
   * choose witch Card should be played with the right Game logic.
   */
  public int welcheKarteSpielIch(int meinePosition,
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten) {
    System.out.println("Ich spiele eine Karte Sauspiel");

    int spielerNummer = tischKarten.size();

    if (istSpieler && tischKarten.getLast().getFarbe().equals(suchFarbe)) {
      return farbeZugeben(meineHand, suchFarbe, 2);
    }

    switch (spielerNummer) {
      case 0:
        if (istSpieler) {
          return meineHand.size() - 1;
        } else {
          return 0;
        }
      case 1:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
        }
      case 2:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
        }
      case 3:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
        }
      default:
        System.out.println("Ungültige SpielerNummer");
    }
    return 0;
  }
}
