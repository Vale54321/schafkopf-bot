package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.karte.Karte;
import org.example.karte.KartenFarbe;
import org.example.karte.KartenUtil;
import org.example.spielController.*;

public class Schafkopf {
    private SpielController spiel = new SauSpielController(KartenFarbe.EICHEL, false);
    private final BackendServer server;
    private boolean gameState = false;

    private Thread spielThread;

    Schafkopf(BackendServer server) {
        this.server = server;
        System.out.println("SchaffKopfGame erstellt");
    }

    public void showTrumpf() {
        server.sendMessageToAllFrontendEndpoints(spiel.getTrumpfKarten().getJson());
    }

    public void showFarbe() {
        server.sendMessageToAllFrontendEndpoints(spiel.getFarbKarten().getJson());
    }

    public void testHand() {
        wartetAufKarte().getJson();
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
        server.sendMessageToAllFrontendEndpoints(karte.getJson());
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
            spielThread = new Thread(() -> new Spielablauf(this, spiel, KartenUtil.zieheZufallsHand(4), 0, true));

            spielThread.start();
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

        spielThread.interrupt();
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
