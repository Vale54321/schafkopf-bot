package org.example;

import org.example.karte.Karte;
import org.example.karte.KartenListe;
import org.example.karte.KartenUtil;
import org.example.spielController.SauSpielController;
import org.example.spielController.SpielController;

import java.util.ArrayList;
import java.util.List;

public class Spielablauf {
    private KartenListe gespielteKarten, eigeneKarten, tischKarten, unbekannteKarten;
    private SpielController spiel;

    Schafkopf schafkopf;
    private int startSpieler;
    private boolean aktiverSpieler; //ob der bot Spieler ist oder nicht

    private int gemachteStiche;


    Spielablauf(Schafkopf schafkopf, SpielController spiel, KartenListe hand, int startSpieler, boolean aktiverSpieler) {
        this.schafkopf=schafkopf;
        this.spiel=spiel;
        this.startSpieler=startSpieler;
        this.aktiverSpieler=aktiverSpieler;
        this.eigeneKarten=hand;
        KartenListe alleKarten = KartenUtil.initializeSchafKopfCardDeck();
        this.unbekannteKarten=new KartenListe(alleKarten);
        this.unbekannteKarten.removeKarten(this.eigeneKarten);
        gespielteKarten=new KartenListe();
        tischKarten=new KartenListe();
        gemachteStiche=0;
        try {
            einStich();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void einStich() throws InterruptedException {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Starte Stiche");
        while(gemachteStiche<8){
            schafkopf.getServer().sendMessageToAllFrontendEndpoints("gespielte Karten:");
            schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
            Thread.sleep(5000);
            System.out.println("Stich: " + gemachteStiche);
            int i = startSpieler;
            while (i % 4 != 0) {
                System.out.println("warte auf Karte von Spieler " + i);
                Karte gelegteKarte = schafkopf.wartetAufKarte(); //warten auf Karteneingabe
                System.out.println("erwartete Karte angekommen");
                tischKarten.addKarten(gelegteKarte);
//                unbekannteKarten.removeKarten(gelegteKarte);
                i++;
            }
            System.out.println("Eigene Karte legen");
            //eigeneKarteLegen
            schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
            int eigeneGelegteKarte = spiel.welcheKarteSpielIch(gespielteKarten, eigeneKarten, tischKarten); //Welche KArte soll der Bot spielen, Achtung! unterschiedliche Länge der Listen pro Anfrage, aktiverSpieler hinzufügen
            schafkopf.getServer().sendMessageToAllFrontendEndpoints(eigeneKarten.getByIndex(eigeneGelegteKarte).getJson());
            tischKarten.addKarten(eigeneKarten.getByIndex(eigeneGelegteKarte));
            eigeneKarten.removeKarten(eigeneKarten.getByIndex(eigeneGelegteKarte));

            while (tischKarten.size() < 4) { //tischkarten zählen
                System.out.println("warte auf Karte von Spieler " + tischKarten.size());
                Karte gelegteKarte = schafkopf.wartetAufKarte(); //warten auf Karteneingabe
                System.out.println("erwartete Karte angekommen");
                tischKarten.addKarten(gelegteKarte);
//                unbekannteKarten.removeKarten(gelegteKarte);
            }
            System.out.println("Stiche ende");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

//            int stichSpieler = spiel.werHatGestochen(tischKarten); //returns int(0,1,2,3)
            int stichSpieler = 0;
            startSpieler = startSpieler + stichSpieler;
            if (startSpieler > 3) {
                startSpieler = startSpieler % 4;
            }

            gespielteKarten.addKarten(tischKarten);
            tischKarten.clear();

            gemachteStiche++;
        }
    }



}
