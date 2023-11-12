package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class EventEndpoint extends WebSocketAdapter {
    private static final CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    private Schafkopf schafkopf;

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        String clientIp = session.getRemoteAddress().toString();
        System.out.println("Endpoint connected from ip: " + clientIp);

        sessions.add(session);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message:" + message);

        if (message.contains("startsimulation")) {
            Gson gson = new Gson();
            String jsonData = gson.toJson("Start");
            sendMessage(jsonData);
        }

        if (message.contains("stopsimulation")) {
            Gson gson = new Gson();
            String jsonData = gson.toJson("Stop");
            sendMessage(jsonData);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        sessions.remove(getSession());
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

    public void sendMessage(String message) {
        for (Session session : sessions) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}