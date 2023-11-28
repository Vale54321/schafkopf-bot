package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenFarbe;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;

/** Base Class of Game Controllers. */
public abstract class SpielController {
  protected static KartenListe trumpfKarten;
  protected static KartenListe farbKarten;

  protected static int activePlayer;

  public SpielController(int activePlayer) {
    this.activePlayer = activePlayer;
  }

  /**
   * Create instance of SpielController.
   *
   * @param meineHand Cards one Player holds.
   * @param farbe color the Player has to play.
   * @param mode Mode the player chooses a Card if multiple are available.
   */
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

  /**
   * sorts Cards, so they are in the right order for the active game.
   *
   * @param karten Trumpffarbe of the Farb Geier.
   */
  public static void sortiereKarten(KartenListe karten) {
    KartenListe kartenReihenfolge = new KartenListe(farbKarten);
    kartenReihenfolge.addKarten(trumpfKarten);

    KartenListe kartenListe = KartenUtil.initializeSchafKopfCardDeck();

    kartenListe.removeKarten(karten);
    kartenReihenfolge.removeKarten(kartenListe);

    karten.clear();
    karten.addKarten(kartenReihenfolge);
  }

  /**
   * checks, which card has the highest strength and will win one Stich.
   *
   * @param karten Cards to check.
   */
  public static int welcheKarteSticht(KartenListe karten) {
    KartenListe kartenNew = new KartenListe(karten);
    sortiereKarten(kartenNew);
    KartenListe farbTischKarten = kartenNew.removeKarten(trumpfKarten);
    System.out.println("trumpfKarten:");
    System.out.println(trumpfKarten.getJson());

    if (!farbTischKarten.isEmpty()) {
      System.out.println("trumpfkarten:");
      System.out.println(farbTischKarten.getJson());
      return karten.indexOf(farbTischKarten.getLast());
    } else {
      KartenFarbe firstColor = karten.getByIndex(0).getFarbe();
      KartenListe firstColorCards = kartenNew.removeKarten(firstColor);

      System.out.println("firstcolor:");
      System.out.println(firstColorCards.getJson());

      return karten.indexOf(firstColorCards.getLast());
    }
  }

  public abstract int welcheKarteSpielIch(
      int meinePosition,
      KartenListe gespielteKarten,
      KartenListe meineHand,
      KartenListe tischKarten);

  public KartenListe getTrumpfKarten() {
    return trumpfKarten;
  }

  public KartenListe getFarbKarten() {
    return farbKarten;
  }
}
