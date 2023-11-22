package org.example.karte;

import java.util.Random;

/**
 * Class that brings usefully functions for Card/s.
 */
public class KartenUtil {

  /**
   * initialize a normal Card Deck. It will be in the standard order.
   */
  public static KartenListe initializeSchafKopfCardDeck() {
    KartenFarbe[] farben = {
      KartenFarbe.SCHELL, KartenFarbe.HERZ, KartenFarbe.BLATT, KartenFarbe.EICHEL,
    };
    KartenSymbol[] symbole = {
      KartenSymbol.SIX,
      KartenSymbol.SEVEN,
      KartenSymbol.EIGHT,
      KartenSymbol.NINE,
      KartenSymbol.UNTER,
      KartenSymbol.OBER,
      KartenSymbol.KOENIG,
      KartenSymbol.TEN,
      KartenSymbol.ASS
    };

    KartenListe deck = new KartenListe();

    for (KartenFarbe farbe : farben) {
      for (KartenSymbol symbol : symbole) {
        deck.addKarten(new Karte(farbe, symbol));
      }
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
  public static String getIdOfUid(String uid) {
    return switch (uid) {
      case "04A56BB4780000" -> "eichel_7";
      case "04A46BB4780000" -> "eichel_8";
      case "04A26BB4780000" -> "eichel_9";
      case "04A16BB4780000" -> "eichel_x";
      case "049E6BB4780000" -> "eichel_k";
      case "04A86BB4780000" -> "eichel_a";
      case "04A06BB4780000" -> "eichel_u";
      case "049F6BB4780000" -> "eichel_o";
      case "04F26BB4780000" -> "blatt_7";
      case "04A76BB4780000" -> "blatt_8";
      case "049B6BB4780000" -> "blatt_9";
      case "04996BB4780000" -> "blatt_x";
      case "041CD2C2126F81" -> "blatt_k";
      case "04A96BB4780000" -> "blatt_a";
      case "049A6BB4780000" -> "blatt_u";
      case "049D6BB4780000" -> "blatt_o";
      case "04936BB4780000" -> "schell_7";
      case "04F697C2126F80" -> "schell_8";
      case "04946BB4780000" -> "schell_9";
      case "04956BB4780000" -> "schell_x";
      case "04986BB4780000" -> "schell_k";
      case "04AA6BB4780000" -> "schell_a";
      case "04966BB4780000" -> "schell_u";
      case "04976BB4780000" -> "schell_o";
      case "04F36BB4780000" -> "herz_7";
      case "04B06BB4780000" -> "herz_8";
      case "04AF6BB4780000" -> "herz_9";
      case "04AE6BB4780000" -> "herz_x";
      case "04AB6BB4780000" -> "herz_k";
      case "049C6BB4780000" -> "herz_a";
      case "04AD6BB4780000" -> "herz_u";
      case "04AC6BB4780000" -> "herz_o";
      default -> null;
    };
  }

  /**
   * returns a Card matching an ID.
   *
   * @param id card ID.
   */
  public static Karte getKarteById(String id) {
    KartenListe kartenList = initializeSchafKopfCardDeck();
    for (Karte karte : kartenList.getKartenListe()) {
      if (karte.getId().equalsIgnoreCase(id)) {
        return karte;
      }
    }
    return null;
  }
}
