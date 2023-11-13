package org.example;

import org.example.spielController.SauSpielController;
import org.example.spielController.SpielController;

import java.util.ArrayList;
import java.util.List;

public class Spielablauf {
    private List<Karte> gespielteKarten, eigeneKarten, tischKarten, unbekannteKarten;
    private SpielController spiel;
    private int startSpieler;
    private boolean aktiverSpieler;


    Spielablauf(SpielController spiel, List<Karte> hand, int startSpieler, boolean aktiverSpieler) {
        this.spiel=spiel;
        this.startSpieler=startSpieler;
        this.aktiverSpieler=aktiverSpieler;
        this.eigeneKarten=hand;
        List<Karte> alleKarten = KartenUtil.initializeSchafKopfCardDeck();
        this.unbekannteKarten=new ArrayList<>(alleKarten);
        this.unbekannteKarten.removeAll(this.eigeneKarten);
        einStich(startSpieler);
    }

    public void einStich(int startSpieler){
        int i=startSpieler;
        while(i%4!=0){
            Karte gelegteKarte = null; //warten auf Karteneingabe
            tischKarten.add(gelegteKarte);
            unbekannteKarten.remove(gelegteKarte);
            i++;
        }

        //eigeneKarteLegen
        Karte eigeneGelegteKarte = spiel.welcheKarteSpielIch(gespielteKarten, eigeneKarten, tischKarten, unbekannteKarten); //Welche KArte soll der Bot spielen
        tischKarten.add(eigeneGelegteKarte);
        eigeneKarten.remove(eigeneGelegteKarte);

        while (tischKarten.size()<4){ //tischkarten zählen
            Karte gelegteKarte = null; //warten auf Karteneingabe
            tischKarten.add(gelegteKarte);
            unbekannteKarten.remove(gelegteKarte);
        }

        gespielteKarten.add(null);

    }



}
