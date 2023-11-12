package org.example;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import java.io.IOException;

public class Schafkopf {
    private Karte[] deck;
    private Session session;
    Schafkopf(Session session){
        this.session = session;
    }
    public void initializeCardDeck() throws IOException {
        Gson gson = new Gson();
        String jsonData = gson.toJson("Initialisiere Kartendeck");
        session.getRemote().sendString(jsonData);
    }
}
