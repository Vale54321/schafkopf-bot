package org.example;

import com.google.gson.Gson;
import org.example.spielController.SauSpielController;
import org.example.spielController.SpielController;

import java.util.ArrayList;
import java.util.List;

public class Schafkopf {
    private final SpielController spielController = new SauSpielController("eichel", false);
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
    }

    public void showFarbe() {
        List<Karte> testHand = KartenUtil.zieheZufallsHand(8);
        KartenUtil.sortiereKarten(testHand, spielController.getFarbKarten(), spielController.getTrumpfKarten());
        Gson gson = new Gson();
        for (Karte karte : testHand) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
        List<Karte> testTischKarten = new ArrayList<>();
        testTischKarten.add(new Karte("eichel_9","Bla","eichel","9",0));
        String karteJson = gson.toJson(spielController.welcheKarteSpielich(new ArrayList<Karte>(), testHand, testTischKarten));
        server.sendMessageToAllFrontendEndpoints(karteJson);

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
