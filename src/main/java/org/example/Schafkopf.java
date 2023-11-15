package org.example;

import com.google.gson.Gson;
import org.example.spielController.*;

import java.util.ArrayList;
import java.util.List;

public class Schafkopf {
    private SpielController spielController = new SauSpielController("eichel", false);
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
        Gson gson = new Gson();
        for (Karte karte : spielController.getFarbKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void testHand() {
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

    public void setGame(String message) {
        System.out.println("Set Game: " + message);
        switch (message)
        {
            case "setgame:herzsolo":
                this.spielController = new FarbSoloController("herz");
                break;
            case "setgame:blattsolo":
                this.spielController = new FarbSoloController("blatt");
                break;
            case "setgame:eichelsolo":
                this.spielController = new FarbSoloController("eichel");
                break;
            case "setgame:shellsolo":
                this.spielController = new FarbSoloController("schell");
                break;


            case "setgame:wenz":
                this.spielController = new WenzController();
                break;
            case "setgame:geier":
                this.spielController = new GeierController();
                break;


            case "setgame:eichelwenz":
                this.spielController = new FarbWenzController("eichel");
                break;
            case "setgame:herzwenz":
                this.spielController = new FarbWenzController("herz");
                break;
            case "setgame:blattwenz":
                this.spielController = new FarbWenzController("blatt");
                break;
            case "setgame:schellwenz":
                this.spielController = new FarbWenzController("schell");
                break;


            case "setgame:eichelgeier":
                this.spielController = new FarbGeierController("eichel");
                break;
            case "setgame:herzgeier":
                this.spielController = new FarbGeierController("herz");
                break;
            case "setgame:blattgeier":
                this.spielController = new FarbGeierController("blatt");
                break;
            case "setgame:schellgeier":
                this.spielController = new FarbGeierController("schell");
                break;



            case "setgame:sauspiel":
                this.spielController = new SauSpielController("eichel", false);
                break;
            default:
                System.out.println("Ungültiges Spiel");
        }
    }
}
