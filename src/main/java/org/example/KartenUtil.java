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

    public static void removeCards(List<Karte> allCards, List<Karte> cardsToRemove) {
        allCards.removeAll(cardsToRemove);
    }
}