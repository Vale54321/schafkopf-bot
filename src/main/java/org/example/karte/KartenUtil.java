package org.example.karte;

import java.util.*;

public class KartenUtil {

    public static KartenListe initializeSchafKopfCardDeck() {
        KartenFarbe[] farben = { KartenFarbe.SCHELL, KartenFarbe.BLATT, KartenFarbe.HERZ, KartenFarbe.EICHEL,};
        KartenSymbol[] symbole = {KartenSymbol.SIX, KartenSymbol.SEVEN, KartenSymbol.EIGHT, KartenSymbol.NINE, KartenSymbol.UNTER, KartenSymbol.OBER, KartenSymbol.KOENIG, KartenSymbol.TEN, KartenSymbol.ASS};

        KartenListe deck = new KartenListe();

        for (KartenFarbe farbe : farben) {
            for (KartenSymbol symbol : symbole) {
                deck.addKarten(new Karte(farbe, symbol));
            }
        }
        deck.removeKarten(KartenSymbol.SIX);
        return deck;
    }

    public static KartenListe zieheZufallsHand(int anzahl) {
        KartenListe karten = initializeSchafKopfCardDeck();
        KartenListe gezogeneKarten = new KartenListe();
        Random random = new Random();

        // Ziehe zufällige Karten
        for (int i = 0; i < anzahl; i++) {
            int zufallsIndex = random.nextInt(karten.size());
            Karte gezogeneKarte = karten.getByIndex(zufallsIndex);
            gezogeneKarten.addKarten(gezogeneKarte);
            karten.removeKarten(gezogeneKarte);
        }
        return gezogeneKarten;
    }

    public static String getIdOfUid(String uid){
        switch (uid){
            case "04A56BB4780000":
                return "eichel_7";
            case "04A46BB4780000":
                return "eichel_8";
            case "04A26BB4780000":
                return "eichel_9";
            case "04A16BB4780000":
                return "eichel_x";
            case "049E6BB4780000":
                return "eichel_k";
            case "04A86BB4780000":
                return "eichel_a";
            case "04A06BB4780000":
                return "eichel_u";
            case "049F6BB4780000":
                return "eichel_o";

            case "04F26BB4780000":
                return "blatt_7";
            case "04A76BB4780000":
                return "blatt_8";
            case "049B6BB4780000":
                return "blatt_9";
            case "04996BB4780000":
                return "blatt_x";
            case "041CD2C2126F81":
                return "blatt_k";
            case "04A96BB4780000":
                return "blatt_a";
            case "049A6BB4780000":
                return "blatt_u";
            case "049D6BB4780000":
                return "blatt_o";

            case "04936BB4780000":
                return "schell_7";
            case "04F697C2126F80":
                return "schell_8";
            case "04946BB4780000":
                return "schell_9";
            case "04956BB4780000":
                return "schell_x";
            case "04986BB4780000":
                return "schell_k";
            case "04AA6BB4780000":
                return "schell_a";
            case "04966BB4780000":
                return "schell_u";
            case "04976BB4780000":
                return "schell_o";

            case "04F36BB4780000":
                return "herz_7";
            case "04B06BB4780000":
                return "herz_8";
            case "04AF6BB4780000":
                return "herz_9";
            case "04AE6BB4780000":
                return "herz_x";
            case "04AB6BB4780000":
                return "herz_k";
            case "049C6BB4780000":
                return "herz_a";
            case "04AD6BB4780000":
                return "herz_u";
            case "04AC6BB4780000":
                return "herz_o";
        }
        return null;
    }

    public static Karte getKarteById(String id){
        KartenListe kartenList = initializeSchafKopfCardDeck();
        for (Karte karte : kartenList.getKartenListe()) {
            if (karte.getId().equalsIgnoreCase(id)) {
                return karte;
            }
        }
        return null;
    }
}