package org.example;

import com.google.gson.Gson;

public class Schafkopf {
    private Karte[] cardDeck;
    private boolean gameState = false;
    private BackendServer server;
    Schafkopf(BackendServer server){
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
        initializeCardDeck();
    }
    public void initializeCardDeck() {
        String[] suits = {"schell", "herz", "blatt", "eichel"};
        String[] values = {"7", "8", "9", "k", "x", "a", "u", "o"};

        int totalCards = suits.length * values.length;
        cardDeck = new Karte[totalCards];

        int index = 0;

        for (String suit : suits) {
            for (String value : values) {
                String cardId = suit + "_" + value;
                String cardName = suit + "_" + value;
                cardDeck[index] = new Karte(cardId, cardName, suit);
                index++;
                System.out.println(cardName);
            }
        }
    }

    public void startGame() {
        if (gameState){
            System.out.println("Game already started!");
            server.sendMessageToAllFrontendEndpoints("Game already started!");
        } else {
            gameState = true;
            System.out.println("Start Game");
            server.sendMessageToAllFrontendEndpoints("Start Game");

            // Send JSON for each Karte to all frontend endpoints
            Gson gson = new Gson();
            for (Karte karte : cardDeck) {
                String karteJson = gson.toJson(karte);
                server.sendMessageToAllFrontendEndpoints(karteJson);
            }
        }
    }

    public void stopGame() {
        if (!gameState){
            System.out.println("no active Game!");
            server.sendMessageToAllFrontendEndpoints("no active Game!");
        } else {
            gameState = false;
            System.out.println("Stop Game");
            server.sendMessageToAllFrontendEndpoints("Stop Game");
        }

    }
}
