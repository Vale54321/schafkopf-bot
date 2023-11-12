package org.example;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import java.io.IOException;

public class Schafkopf {
    private Karte[] deck;
    private EventEndpoint endpoint;
    Schafkopf(EventEndpoint endpoint){
        this.endpoint = endpoint;
    }
    public void initializeCardDeck() throws IOException {
        Gson gson = new Gson();
        String jsonData = gson.toJson("Initialisiere Kartendeck");
        endpoint.sendMessage("test");
    }
}
