package org.example;

import com.google.gson.Gson;
import org.example.karte.Karte;
import org.example.karte.KartenFarbe;
import org.example.karte.KartenUtil;
import org.example.spielController.*;

public class Schafkopf {
    private SpielController spiel = new SauSpielController(KartenFarbe.EICHEL, false);
    private final BackendServer server;
    private boolean gameState = false;

    Schafkopf(BackendServer server) {
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
    }

    public void showTrumpf() {
        Gson gson = new Gson();
        for (Karte karte : spiel.getTrumpfKarten().getKartenListe()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void showFarbe() {
        Gson gson = new Gson();
        for (Karte karte : spiel.getFarbKarten().getKartenListe()) {
            String karteJson = gson.toJson(karte);
            server.sendMessageToAllFrontendEndpoints(karteJson);
        }
    }

    public void testHand() {
//        List<Karte> testHand = KartenUtil.zieheZufallsHand(4);
//        //spielController.sortiereKarten(testHand);
//
//        for (Karte karte : testHand) {
//            String karteJson = gson.toJson(karte);
//            server.sendMessageToAllFrontendEndpoints(karteJson);
//        }
            Gson gson = new Gson();
            try {
                Karte scanKarte = KartenUtil.getKarteById(KartenUtil.getIdOfUid(server.waitForCardScan()));
                server.sendMessageToAllFrontendEndpoints(gson.toJson(scanKarte));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


//        List<Karte> testTischKarten = new ArrayList<>();
//        testTischKarten.add(new Karte("eichel_9","Bla","eichel","9",0));
//        int test = spiel.welcheKarteSpielich(new ArrayList<Karte>(), testHand, testTischKarten);
//        String karteJson = gson.toJson(testHand.get(test));
//        server.sendMessageToAllFrontendEndpoints(karteJson);

//        server.sendMessageToAllFrontendEndpoints(gson.toJson(testHand.get(spiel.welcheKarteSticht(testHand))));
    }

    public Karte wartetAufKarte() {
        String uid = null;
        System.out.println("Starte Warten auf Karte");
        try{
            uid = server.waitForCardScan();
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        String kartenId = KartenUtil.getIdOfUid(uid);

        if(kartenId == null){
            System.out.println("Ungültige Karte");
            return wartetAufKarte();
        }
        Karte karte = KartenUtil.getKarteById(kartenId);
        Gson gson = new Gson();
        server.sendMessageToAllFrontendEndpoints(gson.toJson(karte));
        System.out.println("Karte gescannt: " + karte.getName());
        System.out.println("Beende Warten auf Karte");
        return KartenUtil.getKarteById(kartenId);
    }

    public void startGame() {
        if (gameState) {
            System.out.println("Game already started!");
            server.sendMessageToAllFrontendEndpoints("Game already started!");
        } else {
            gameState = true;
            System.out.println("Start Game");
            server.sendMessageToAllFrontendEndpoints("Start Game");
            new Thread(() -> {

                new Spielablauf(this, spiel, KartenUtil.zieheZufallsHand(4), 0, true);

            }).start();

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
                this.spiel = new FarbSoloController(KartenFarbe.HERZ);
                break;
            case "setgame:blattsolo":
                this.spiel = new FarbSoloController(KartenFarbe.BLATT);
                break;
            case "setgame:eichelsolo":
                this.spiel = new FarbSoloController(KartenFarbe.EICHEL);
                break;
            case "setgame:shellsolo":
                this.spiel = new FarbSoloController(KartenFarbe.SCHELL);
                break;


            case "setgame:wenz":
                this.spiel = new WenzController();
                break;
            case "setgame:geier":
                this.spiel = new GeierController();
                break;


            case "setgame:eichelwenz":
                this.spiel = new FarbWenzController(KartenFarbe.EICHEL);
                break;
            case "setgame:herzwenz":
                this.spiel = new FarbWenzController(KartenFarbe.HERZ);
                break;
            case "setgame:blattwenz":
                this.spiel = new FarbWenzController(KartenFarbe.BLATT);
                break;
            case "setgame:schellwenz":
                this.spiel = new FarbWenzController(KartenFarbe.SCHELL);
                break;


            case "setgame:eichelgeier":
                this.spiel = new FarbGeierController(KartenFarbe.EICHEL);
                break;
            case "setgame:herzgeier":
                this.spiel = new FarbGeierController(KartenFarbe.HERZ);
                break;
            case "setgame:blattgeier":
                this.spiel = new FarbGeierController(KartenFarbe.BLATT);
                break;
            case "setgame:schellgeier":
                this.spiel = new FarbGeierController(KartenFarbe.SCHELL);
                break;



            case "setgame:sauspiel":
                this.spiel = new SauSpielController(KartenFarbe.EICHEL, false);
                break;
            default:
                System.out.println("Ungültiges Spiel");
        }
    }
}
