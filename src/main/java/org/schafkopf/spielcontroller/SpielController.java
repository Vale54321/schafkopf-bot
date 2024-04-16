package org.schafkopf.spielcontroller;

import org.schafkopf.karte.Karte;
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
   * @param ersteKarte color the Player has to play.
   * @param mode Mode the player chooses a Card if multiple are available.
   */
  public static Karte farbeZugeben(KartenListe meineHand, Karte ersteKarte, int mode) {
    KartenListe hand = new KartenListe(meineHand);
    sortiereKarten(hand);

    boolean trumpfGespielt = trumpfKarten.containsKarte(ersteKarte);

    KartenListe handTrumpfKarten = hand.removeKarten(trumpfKarten);
    KartenListe handfarbKarten;

    if (trumpfGespielt) {
      handfarbKarten = handTrumpfKarten;
    } else {
      handfarbKarten = hand.getKarten(ersteKarte.getFarbe());
    }

    if (handfarbKarten.size() == 1) {
      return handfarbKarten.getByIndex(0);
    } else if (handfarbKarten.size() > 1) {
      return switch (mode) {
        case 0 -> // Abspatzen
        handfarbKarten.getByIndex(0);
        case 1, 2 -> // Stechen // Schmieren
        handfarbKarten.getLast();
        default -> null;
      };
    }
    if (handfarbKarten.isEmpty()) {
      switch (mode) {
        case 0: // Abspatzen
          return hand.getByIndex(0);
        case 1: // Schmieren
          return hand.getLast();
        case 2: // Stechen
          if (!handTrumpfKarten.isEmpty()) {
            return handTrumpfKarten.getLast(); // trumpf reinspielen
          } else {
            return hand.getByIndex(0); // wenn kein Trumpf und farblos, abschpatzen
          }
        default:
          return null;
      }
    }
    return null;
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

  public abstract Karte welcheKarteSpielIch(
      boolean istSpieler,
      KartenListe gespielteKarten,
      KartenListe meineHand,
      KartenListe tischKarten);

  public KartenListe getTrumpfKarten() {
    return trumpfKarten;
  }

  public boolean isTrumpf(Karte card) {
    return trumpfKarten.containsKarte(card);
  }

  public KartenListe getFarbKarten() {
    return farbKarten;
  }
}
