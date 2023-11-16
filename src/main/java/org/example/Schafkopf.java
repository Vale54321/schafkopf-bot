package org.example;

import com.google.gson.Gson;
import org.example.spielController.*;

import java.util.ArrayList;
import java.util.List;

public class Schafkopf {
    private SpielController spiel = new SauSpielController("eichel", false);
    private final BackendServer server;
    private boolean gameState = false;

    Schafkopf(BackendServer server) {
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
    }

    public void showTrumpf() {
        Gson gson = new Gson();
        for (Karte karte : spiel.getTrumpfKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void showFarbe() {
        Gson gson = new Gson();
        for (Karte karte : spiel.getFarbKarten()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void testHand() {
        List<Karte> testHand = KartenUtil.zieheZufallsHand(4);
        //spielController.sortiereKarten(testHand);
        Gson gson = new Gson();
        for (Karte karte : testHand) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }

        while(true){
            try {
                Karte scanKarte = KartenUtil.getKarteById(KartenUtil.getIdOfUid(server.waitForCardScan()));
                server.sendMessageToAllFrontendEndpoints(gson.toJson(scanKarte));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        List<Karte> testTischKarten = new ArrayList<>();
//        testTischKarten.add(new Karte("eichel_9","Bla","eichel","9",0));
//        int test = spiel.welcheKarteSpielich(new ArrayList<Karte>(), testHand, testTischKarten);
//        String karteJson = gson.toJson(testHand.get(test));
//        server.sendMessageToAllFrontendEndpoints(karteJson);

//        server.sendMessageToAllFrontendEndpoints(gson.toJson(testHand.get(spiel.welcheKarteSticht(testHand))));
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
                this.spiel = new FarbSoloController("herz");
                break;
            case "setgame:blattsolo":
                this.spiel = new FarbSoloController("blatt");
                break;
            case "setgame:eichelsolo":
                this.spiel = new FarbSoloController("eichel");
                break;
            case "setgame:shellsolo":
                this.spiel = new FarbSoloController("schell");
                break;


            case "setgame:wenz":
                this.spiel = new WenzController();
                break;
            case "setgame:geier":
                this.spiel = new GeierController();
                break;


            case "setgame:eichelwenz":
                this.spiel = new FarbWenzController("eichel");
                break;
            case "setgame:herzwenz":
                this.spiel = new FarbWenzController("herz");
                break;
            case "setgame:blattwenz":
                this.spiel = new FarbWenzController("blatt");
                break;
            case "setgame:schellwenz":
                this.spiel = new FarbWenzController("schell");
                break;


            case "setgame:eichelgeier":
                this.spiel = new FarbGeierController("eichel");
                break;
            case "setgame:herzgeier":
                this.spiel = new FarbGeierController("herz");
                break;
            case "setgame:blattgeier":
                this.spiel = new FarbGeierController("blatt");
                break;
            case "setgame:schellgeier":
                this.spiel = new FarbGeierController("schell");
                break;



            case "setgame:sauspiel":
                this.spiel = new SauSpielController("eichel", false);
                break;
            default:
                System.out.println("Ungültiges Spiel");
        }
    }
}
