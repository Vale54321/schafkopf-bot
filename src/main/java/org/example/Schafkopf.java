package org.example;

import com.google.gson.Gson;
import org.example.spielController.GeierController;
import org.example.spielController.SpielController;

public class Schafkopf {
    private final SpielController spielController = new GeierController();
    private final BackendServer server;
    private boolean gameState = false;

    Schafkopf(BackendServer server) {
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
    }

    public void showTrumpf() {

        Gson gson = new Gson();
        for (Karte karte : spielController.getTrumpfKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
        spielController.welcheKarteSpielich();
    }

    public void showFarbe() {
        Gson gson = new Gson();
        for (Karte karte : spielController.getFarbKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
        spielController.welcheKarteSpielich();
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
