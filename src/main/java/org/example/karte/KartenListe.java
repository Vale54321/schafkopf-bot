package org.example.karte;

import java.util.ArrayList;
import java.util.List;

public class KartenListe {
    private List<Karte> kartenListe;

    public KartenListe() {
        this.kartenListe = new ArrayList<>();
    }

    public KartenListe(KartenListe liste) {
        this.kartenListe = new ArrayList<>(liste.getKartenListe());
    }

    public List<Karte> getKartenListe() {
        return this.kartenListe;
    }

    public void addKarten(Karte karte) {
        if(!this.containsKarte(karte)){
            this.kartenListe.add(karte);
            return;
        }
        throw new RuntimeException("Karte bereits vorhanden");
    }

    //methoden zum hinzufügen von karten

    public void addKarten(KartenListe karten) {
        for (Karte karte : karten.getKartenListe()) {
            this.addKarten(karte);
        }
    }

    public KartenListe removeKarten(KartenListe karten) {
        KartenListe result = new KartenListe();
        for (Karte karteWeg : karten.getKartenListe()) {
            for (Karte karte : this.kartenListe) {
                if (karte.getId().equals(karteWeg.getId())) {
                    result.addKarten(karte);
                    break;
                }
            }
        }
        if (result.getKartenListe().isEmpty()) {
            throw new RuntimeException("Karte haben keine Überschneidung");
        }
        this.kartenListe.removeAll(result.getKartenListe());
        return result;
    }


    //methoden zum entfernen von karten


    public KartenListe removeKarten(KartenFarbe farbe) {
        KartenListe result = new KartenListe();
        for (Karte karte : this.kartenListe) {
            if (karte.getFarbe().equals(farbe)) {
                result.addKarten(karte);
            }
        }
        if (result.getKartenListe().isEmpty()) {
            throw new RuntimeException("Karte haben keine Überschneidung");
        }
        this.kartenListe.removeAll(result.getKartenListe());
        return result;
    }

    public KartenListe removeKarten(KartenSymbol symbol) {
        KartenListe result = new KartenListe();
        for (Karte karte : this.kartenListe) {
            if (karte.getSymbol().equals(symbol)) {
                result.addKarten(karte);
            }
        }
        if (result.isEmpty()) {
            throw new RuntimeException("Karte haben keine Überschneidung");
        }
        this.kartenListe.removeAll(result.getKartenListe());
        return result;
    }

    public Karte removeKarten(Karte karteToRemove) {
        for (Karte karte : this.kartenListe) {
            if (karte.getId().equals(karteToRemove.getId())) {
                this.kartenListe.remove(karte);
                return karte;
            }
        }
        throw new RuntimeException("Karte nicht gefunden");
    }

    public Karte removeKarten(String idToRemove) {
        for (Karte karte : this.kartenListe) {
            if (karte.getId().equals(idToRemove)) {
                this.kartenListe.remove(karte);
                return karte;
            }
        }
        throw new RuntimeException("Karte nicht gefunden");
    }

    private boolean containsKarte(Karte karte) {
        for (Karte karteInListe : this.kartenListe) {
            if (karteInListe.getId().equals(karte.getId())) {
                return true;
            }
        }
        return false;
    }

    //get Karten

    public KartenListe getKarten(KartenFarbe farbe) {
        KartenListe result = new KartenListe();
        for (Karte karte : this.kartenListe) {
            if (karte.getFarbe().equals(farbe)) {
                result.addKarten(karte);
            }
        }
        if (result.getKartenListe().isEmpty()) {
            throw new RuntimeException("Karte haben keine Überschneidung");
        }
        return result;
    }

    public KartenListe getKarten(KartenSymbol symbol) {
        KartenListe result = new KartenListe();
        for (Karte karte : this.kartenListe) {
            if (karte.getSymbol().equals(symbol)) {
                result.addKarten(karte);
            }
        }
        if (result.getKartenListe().isEmpty()) {
            throw new RuntimeException("Karte haben keine Überschneidung");
        }
        return result;
    }

    public boolean isEmpty() {
        return this.kartenListe.isEmpty();
    }

    public Karte getLast() {
        return this.kartenListe.getLast();
    }

    public Karte getByIndex(int index) {
        return this.kartenListe.get(index);
    }

    public int size() {
        return this.kartenListe.size();
    }

    public int indexOf(Karte karte) {
        for(Karte karteInListe : this.kartenListe){
            if(karteInListe.getId().equals(karte.getId())){
                return this.kartenListe.indexOf(karteInListe);
            }
        }
        return -1;
    }

    public void clear() {
        this.kartenListe.clear();
    }
}
