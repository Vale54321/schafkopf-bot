package org.example;

import org.example.spielController.SauSpielController;
import org.example.spielController.SpielController;

import java.util.ArrayList;
import java.util.List;

public class Spielablauf {
    private List<Karte> gespielteKarten, eigeneKarten, tischKarten, unbekannteKarten;
    private SpielController spiel;
    private int startSpieler;
    private boolean aktiverSpieler; //ob der bot Spieler ist oder nicht

    private int gemachteStiche;


    Spielablauf(SpielController spiel, List<Karte> hand, int startSpieler, boolean aktiverSpieler) {
        this.spiel=spiel;
        this.startSpieler=startSpieler;
        this.aktiverSpieler=aktiverSpieler;
        this.eigeneKarten=hand;
        List<Karte> alleKarten = KartenUtil.initializeSchafKopfCardDeck();
        this.unbekannteKarten=new ArrayList<>(alleKarten);
        this.unbekannteKarten.removeAll(this.eigeneKarten);
        gespielteKarten=new ArrayList<>();
        tischKarten=new ArrayList<>();
        gemachteStiche=0;
        einStich();
    }

    public void einStich(){
        while(gemachteStiche<8){
            int i = startSpieler;
            while (i % 4 != 0) {
                Karte gelegteKarte = null; //warten auf Karteneingabe
                tischKarten.add(gelegteKarte);
                unbekannteKarten.remove(gelegteKarte);
                i++;
            }

            //eigeneKarteLegen
            Karte eigeneGelegteKarte = spiel.welcheKarteSpielIch(gespielteKarten, eigeneKarten, tischKarten, unbekannteKarten, aktiverSpieler); //Welche KArte soll der Bot spielen, Achtung! unterschiedliche Länge der Listen pro Anfrage
            tischKarten.add(eigeneGelegteKarte);
            eigeneKarten.remove(eigeneGelegteKarte);

            while (tischKarten.size() < 4) { //tischkarten zählen
                Karte gelegteKarte = null; //warten auf Karteneingabe
                tischKarten.add(gelegteKarte);
                unbekannteKarten.remove(gelegteKarte);
            }

            int stichSpieler = spiel.werHatGestochen(tischKarten); //returns int(0,1,2,3)
            startSpieler = startSpieler + stichSpieler;
            if (startSpieler > 3) {
                startSpieler = startSpieler % 4;
            }

            gespielteKarten.addAll(tischKarten);
            tischKarten.clear();

            gemachteStiche++;
        }
    }



}
