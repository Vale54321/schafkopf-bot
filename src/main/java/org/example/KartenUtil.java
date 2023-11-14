package org.example;

import java.util.*;

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

    public static ArrayList<Karte> zieheZufallsHand(int anzahl) {
        List<Karte> karten = initializeSchafKopfCardDeck();
        ArrayList<Karte> gezogeneKarten = new ArrayList<>();
        Random random = new Random();

        // Ziehe zufällige Karten
        for (int i = 0; i < anzahl; i++) {
            int zufallsIndex = random.nextInt(karten.size());
            Karte gezogeneKarte = karten.get(zufallsIndex);
            gezogeneKarten.add(gezogeneKarte);
            karten.remove(gezogeneKarte);
        }
        return gezogeneKarten;
    }

    public static void removeKarten(List<Karte> karten, List<Karte> kartenWeg){
        for (Karte karteWeg : kartenWeg) {
            for (Karte karte : karten) {
                if(karte.getId().equals(karteWeg.getId())){
                    karten.remove(karte);
                    break;
                }
            }
        }
    }

    public static void sortiereKarten(List<Karte> karten, List<Karte> farbkarten, List<Karte> trumpfkarten) {
        List<Karte> kartenReihenfolge = new ArrayList<>(farbkarten);
        kartenReihenfolge.addAll(trumpfkarten);

        List<Karte> kartenListe = initializeSchafKopfCardDeck();

        removeKarten(kartenListe, karten);
        removeKarten(kartenReihenfolge, kartenListe);

        karten.clear();
        karten.addAll(kartenReihenfolge);
    }
}