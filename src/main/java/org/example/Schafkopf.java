package org.example;

import com.google.gson.Gson;
import org.example.spielController.GeierSpielController;
import org.example.spielController.SoloSpielController;
import org.example.spielController.SpielController;
import org.example.spielController.WenzSpielController;

public class Schafkopf {
    private final Karte[] kartenDeck;
    private final SpielController spielController = new GeierSpielController(  "schell");
    private final BackendServer server;
    private boolean gameState = false;

    Schafkopf(BackendServer server) {
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
        this.kartenDeck = initializeCardDeck();
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

    public Karte[] initializeCardDeck() {
        String[] farben = {"schell", "herz", "blatt", "eichel"};
        String[] symbole = {"6", "7", "8", "9", "k", "x", "a", "u", "o"};

        int totalCards = farben.length * symbole.length;
        Karte[] Deck = new Karte[totalCards];

        int index = 0;

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
                Deck[index] = new Karte(cardId, cardName, farbe, symbol, wert);
                index++;
            }
        }
        return Deck;
    }

    public void showTrumpf() {

//        Gson gson = new Gson();
//        for (Karte karte : spielController.getTrumpfKarten()) {
//            String karteJson = gson.toJson(karte);
//            server.sendMessageToAllFrontendEndpoints(karteJson);
//        }
        while(true) {
            try {
                Karte karte = KartenUtil.getKarteById(KartenUtil.getIdOfUid(server.waitForCardScan()));
                String karteJson = new Gson().toJson(karte);

                server.sendMessageToAllFrontendEndpoints(karteJson);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void showFarbe() {
        Gson gson = new Gson();
        for (Karte karte : spielController.getFarbKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void startGame() {
        if (gameState) {
            System.out.println("Game already started!");
            server.sendMessageToAllFrontendEndpoints("Game already started!");
        } else {
            gameState = true;
            System.out.println("Start Game");
            server.sendMessageToAllFrontendEndpoints("Start Game");
        }
    }

    public void stopGame() {
        if (!gameState) {
            System.out.println("no active Game!");
            server.sendMessageToAllFrontendEndpoints("no active Game!");
        } else {
            gameState = false;
            System.out.println("Stop Game");
            server.sendMessageToAllFrontendEndpoints("Stop Game");
        }

    }
}
