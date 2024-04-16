package org.schafkopf.spielcontroller;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;

/** SpielController that implements Logic of a Sau Spiel Game. */
public class SauSpielController extends StandardController {

  KartenFarbe suchFarbe;

  /** Class that represents one Card of the game. */
  public SauSpielController(int activePlayer, KartenFarbe farbe) {
    super(activePlayer);
    this.suchFarbe = farbe;
  }

  /** choose witch Card should be played with the right Game logic. */
  public Karte welcheKarteSpielIch(
      boolean istSpieler,
      KartenListe gespielteKarten,
      KartenListe meineHand,
      KartenListe tischKarten) {
    System.out.println("Ich spiele eine Karte Sauspiel");

    int spielerNummer = tischKarten.size();

    switch (spielerNummer) {
      case 0:
        if (istSpieler) {
          return meineHand.getLast();
        } else {
          return meineHand.getByIndex(0);
        }
      case 1:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 0);
        }
      case 2:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 0);
        }
      case 3:
        if (istSpieler) {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 2);
        } else {
          return farbeZugeben(meineHand, tischKarten.getByIndex(0), 0);
        }
      default:
        System.out.println("Ungültige SpielerNummer");
    }
    return null;
  }
}
