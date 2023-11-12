package org.example;

public class Karte {
    private String id;
    private String name;
    private String farbe;
    Karte(String id, String name, String farbe){
        this.id = id;
        this.name = name;
        this.farbe = farbe;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getFarbe(){
        return this.farbe;
    }
}
