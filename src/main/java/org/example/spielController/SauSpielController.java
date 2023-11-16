package org.example.spielController;
import org.example.Karte;
import org.example.KartenUtil;

import java.util.List;
import java.util.ArrayList;

public class SauSpielController extends StandardController{

    String suchFarbe;
    boolean istSpieler;

    public SauSpielController(String suchFarbe, boolean istSpieler){
        this.suchFarbe = suchFarbe;
        this.istSpieler = istSpieler;
    }
    public int welcheKarteSpielich(List<Karte> gespielteKarten, List<Karte> meineHand, List<Karte> tischKarten){
        System.out.println("Ich spiele eine Karte Sauspiel");

        int spielerNummer = tischKarten.size();

        if (istSpieler && tischKarten.getLast().getFarbe().equals(suchFarbe)) {
            return farbeZugeben(meineHand, suchFarbe, 2);
        }

        switch (spielerNummer) {
            case 0:
                if (istSpieler) {
                    return meineHand.size() - 1;
                } else {
                    return 0;
                }
            case 1:
                if (istSpieler) {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
                } else {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
                }
            case 2:
                if (istSpieler) {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
                } else {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
                }
            case 3:
                if (istSpieler) {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 2);
                } else {
                    return farbeZugeben(meineHand, tischKarten.getLast().getFarbe(), 0);
                }
            default:
                System.out.println("Ungültige SpielerNummer");
        }
        return 0;
    }
}
