package org.example.spielcontroller;

import org.example.karte.KartenFarbe;
import org.example.karte.KartenListe;
import org.example.karte.KartenUtil;

public abstract class SpielController {
  protected static KartenListe trumpfKarten;
  protected static KartenListe farbKarten;

  public static int farbeZugeben(KartenListe meineHand, KartenFarbe farbe, int mode) {
    KartenListe farbKarten = meineHand.getKarten(farbe);
    farbKarten.removeKarten(trumpfKarten);
    if (farbKarten.size() == 1) {
      return meineHand.indexOf(farbKarten.getByIndex(0));
    }
    if (farbKarten.size() > 1) {
      switch (mode) {
        case 0:
          return 0;
        case 1:
          return meineHand.indexOf(farbKarten.getLast());
        case 2:
          return meineHand.indexOf(farbKarten.getLast());
        default:
          return 0;
      }
    }
    if (farbKarten.isEmpty()) {
      switch (mode) {
        case 0:
          return 0;
        case 1:
          return 0;
        case 2:
          return meineHand.size() - 1;
        default:
          return 0;
      }
    }
    return 0;
  }

  public static void sortiereKarten(KartenListe karten) {
    KartenListe kartenReihenfolge = new KartenListe(farbKarten);
    kartenReihenfolge.addKarten(trumpfKarten);

    KartenListe kartenListe = KartenUtil.initializeSchafKopfCardDeck();

    kartenListe.removeKarten(karten);
    kartenReihenfolge.removeKarten(kartenListe);

    karten.clear();
    karten.addKarten(kartenReihenfolge);
  }

  public static int welcheKarteSticht(KartenListe karten) {
    KartenListe kartenNew = new KartenListe(karten);
    sortiereKarten(kartenNew);

    int i = karten.indexOf(kartenNew.getLast());

    System.out.println("karte sticht: " + i);
    return i;
  }

  public abstract int welcheKarteSpielIch(
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten);

  public KartenListe getTrumpfKarten() {
    return trumpfKarten;
  }

  public KartenListe getFarbKarten() {
    return farbKarten;
  }
}
