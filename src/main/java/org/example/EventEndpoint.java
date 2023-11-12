package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class EventEndpoint extends WebSocketAdapter {
    private static final CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(EventEndpoint.class);
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    private Schafkopf schafkopf;

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        LOG.debug("Endpoint connected: {}", session);
        System.out.println("Endpoint connected:" + session);

        sessions.add(session);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        LOG.debug("Received TEXT message: {}", message);
        System.out.println("Received TEXT message:" + message);


        for (Session session : sessions) {
            if (message.toLowerCase(Locale.US).contains("startsimulation")) {
                try {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson("Start");
                    session.getRemote().sendString(jsonData);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (message.toLowerCase(Locale.US).contains("stopsimulation")) {
                try {
                    Gson gson = new Gson();
                    String jsonData = gson.toJson("Stop");
                    session.getRemote().sendString(jsonData);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        sessions.remove(getSession());
        LOG.debug("Socket Closed: [{}] {}", statusCode, reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException {
        LOG.debug("Awaiting closure from remote");
        closureLatch.await();
    }

    public void sendMessage(String message){
        for (Session session : sessions) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}