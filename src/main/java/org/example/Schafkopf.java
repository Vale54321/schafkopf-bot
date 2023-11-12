package org.example;
import com.google.gson.Gson;
import java.io.IOException;

public class Schafkopf {
    private Karte[] deck;
    private FrontendEndpoint endpoint;
    Schafkopf(FrontendEndpoint endpoint){
        this.endpoint = endpoint;
    }
    public void initializeCardDeck() throws IOException {
        Gson gson = new Gson();
        String jsonData = gson.toJson("Initialisiere Kartendeck");
        endpoint.sendMessage("test");
    }
}
