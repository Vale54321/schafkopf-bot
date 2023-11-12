package org.example;

public class Karte {
    private String id;
    private String name;
    Karte(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
