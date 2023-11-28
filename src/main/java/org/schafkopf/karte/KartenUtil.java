package org.schafkopf.karte;

import java.util.Random;

/** Class that brings usefully functions for Card/s. */
public class KartenUtil {

  /** initialize a normal Card Deck. It will be in the standard order. */
  public static KartenListe initializeSchafKopfCardDeck() {
    KartenListe deck = new KartenListe();

    for (Karte karte : Karte.values()) {
      deck.addKarten(karte);
    }

    deck.removeKarten(KartenSymbol.SIX);
    return deck;
  }

  /**
   * Create a List of Random Cards.
   *
   * @param anzahl count of random cards.
   */
  public static KartenListe zieheZufallsHand(int anzahl) {
    KartenListe karten = initializeSchafKopfCardDeck();
    KartenListe gezogeneKarten = new KartenListe();
    Random random = new Random();

    // Ziehe zufällige Karten
    for (int i = 0; i < anzahl; i++) {
      int zufallsIndex = random.nextInt(karten.size());
      Karte gezogeneKarte = karten.getByIndex(zufallsIndex);
      gezogeneKarten.addKarten(gezogeneKarte);
      karten.removeKarten(gezogeneKarte);
    }
    return gezogeneKarten;
  }

  /**
   * converts Uid from a NFC Card to a card ID.
   *
   * @param uid uId to get the Card ID from.
   */
  public static Karte getIdOfUid(String uid) {
    return switch (uid) {
      case "04A56BB4780000" -> Karte.EICHEL_7;
      case "04A46BB4780000" -> Karte.EICHEL_8;
      case "04A26BB4780000" -> Karte.EICHEL_9;
      case "04A16BB4780000" -> Karte.EICHEL_X;
      case "049E6BB4780000" -> Karte.EICHEL_K;
      case "04A86BB4780000" -> Karte.EICHEL_A;
      case "04A06BB4780000" -> Karte.EICHEL_U;
      case "049F6BB4780000" -> Karte.EICHEL_O;
      case "04F26BB4780000" -> Karte.BLATT_7;
      case "04A76BB4780000" -> Karte.BLATT_8;
      case "049B6BB4780000" -> Karte.BLATT_9;
      case "04996BB4780000" -> Karte.BLATT_X;
      case "041CD2C2126F81" -> Karte.BLATT_K;
      case "04A96BB4780000" -> Karte.BLATT_A;
      case "049A6BB4780000" -> Karte.BLATT_U;
      case "049D6BB4780000" -> Karte.BLATT_O;
      case "04936BB4780000" -> Karte.SCHELL_7;
      case "04F697C2126F80" -> Karte.SCHELL_8;
      case "04946BB4780000" -> Karte.SCHELL_9;
      case "04956BB4780000" -> Karte.SCHELL_X;
      case "04986BB4780000" -> Karte.SCHELL_K;
      case "04AA6BB4780000" -> Karte.SCHELL_A;
      case "04966BB4780000" -> Karte.SCHELL_U;
      case "04976BB4780000" -> Karte.SCHELL_O;
      case "04F36BB4780000" -> Karte.HERZ_7;
      case "04B06BB4780000" -> Karte.HERZ_8;
      case "04AF6BB4780000" -> Karte.HERZ_9;
      case "04AE6BB4780000" -> Karte.HERZ_X;
      case "04AB6BB4780000" -> Karte.HERZ_K;
      case "049C6BB4780000" -> Karte.HERZ_A;
      case "04AD6BB4780000" -> Karte.HERZ_U;
      case "04AC6BB4780000" -> Karte.HERZ_O;
      default -> null;
    };
  }
}
