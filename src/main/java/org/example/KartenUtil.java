package org.example;

import java.util.ArrayList;
import java.util.List;

public class KartenUtil {

    public static List<Karte> getKartenByFarbe(List<Karte> karten, String farbe) {
        List<Karte> result = new ArrayList<>();
        for (Karte karte : karten) {
            if (karte.getFarbe().equalsIgnoreCase(farbe)) {
                result.add(karte);
            }
        }
        return result;
    }

    public static List<Karte> getKartenBySymbol(List<Karte> karten, String symbol) {
        List<Karte> result = new ArrayList<>();
        for (Karte karte : karten) {
            if (karte.getSymbol().equalsIgnoreCase(symbol)) {
                result.add(karte);
            }
        }
        return result;
    }

    public static List<Karte> initializeSchafKopfCardDeck() {
        String[] farben = {"schell", "herz", "blatt", "eichel"};
        String[] symbole = {"6", "7", "8", "9", "u", "o", "k", "x", "a"};

        int totalCards = farben.length * symbole.length;
        List<Karte> deck = new ArrayList<>();

        for (String farbe : farben) {
            for (String symbol : symbole) {
                String cardId = farbe + "_" + symbol;
                String cardName = getCardName(farbe, symbol);
                int wert = switch (symbol) {
                    case "6", "9", "8", "7" -> 0;
                    case "k" -> 4;
                    case "x" -> 10;
                    case "a" -> 11;
                    case "u" -> 2;
                    case "o" -> 3;
                    default -> -1;
                };
                deck.add(new Karte(cardId, cardName, farbe, symbol, wert));
            }
        }
        deck.removeAll(KartenUtil.getKartenBySymbol(deck, "6"));
        return deck;
    }

    private static String getCardName(String farbe, String symbol) {
        String cardName = switch (farbe) {
            case "schell" -> "Schell";
            case "herz" -> "Herz";
            case "blatt" -> "Blatt";
            case "eichel" -> "Eichel";
            default -> "";
        };
        String prefix = switch (symbol) {
            case "6" -> "6";
            case "7" -> "7";
            case "8" -> "8";
            case "9" -> "9";
            case "k" -> "König";
            case "x" -> "10";
            case "a" -> "Ass";
            case "u" -> "Unter";
            case "o" -> "Ober";
            default -> "";
        };
        cardName += " " + prefix;
        return cardName;
    }

    public static String getIdOfUid(String uid){
        switch (uid){
            case "04A56BB4780000":
                return "eichel_7";
            case "04A46BB4780000":
                return "eichel_8";
            case "04A26BB4780000":
                return "eichel_9";
            case "04A16BB4780000":
                return "eichel_x";
            case "049E6BB4780000":
                return "eichel_k";
            case "04A86BB4780000":
                return "eichel_a";
            case "04A06BB4780000":
                return "eichel_u";
            case "049F6BB4780000":
                return "eichel_o";

            case "04F26BB4780000":
                return "blatt_7";
            case "04A76BB4780000":
                return "blatt_8";
            case "049B6BB4780000":
                return "blatt_9";
            case "04996BB4780000":
                return "blatt_x";
            case "041CD2C2126F81":
                return "blatt_k";
            case "04A96BB4780000":
                return "blatt_a";
            case "049A6BB4780000":
                return "blatt_u";
            case "049D6BB4780000":
                return "blatt_o";

            case "04936BB4780000":
                return "schell_7";
            case "04F697C2126F80":
                return "schell_8";
            case "04946BB4780000":
                return "schell_9";
            case "04956BB4780000":
                return "schell_x";
            case "04986BB4780000":
                return "schell_k";
            case "04AA6BB4780000":
                return "schell_a";
            case "04966BB4780000":
                return "schell_u";
            case "04976BB4780000":
                return "schell_o";

            case "04F36BB4780000":
                return "herz_7";
            case "04B06BB4780000":
                return "herz_8";
            case "04AF6BB4780000":
                return "herz_9";
            case "04AE6BB4780000":
                return "herz_x";
            case "04AB6BB4780000":
                return "herz_k";
            case "049C6BB4780000":
                return "herz_a";
            case "04AD6BB4780000":
                return "herz_u";
            case "04AC6BB4780000":
                return "herz_o";
        }
        return null;
    }

    public static Karte getKarteById(String id){
        List<Karte> kartenList = initializeSchafKopfCardDeck();
        for (Karte karte : kartenList) {
            if (karte.getId().equalsIgnoreCase(id)) {
                return karte;
            }
        }
        return null;
    }

    public static void removeCards(List<Karte> allCards, List<Karte> cardsToRemove) {
        allCards.removeAll(cardsToRemove);
    }
}