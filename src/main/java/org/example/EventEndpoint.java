package org.example;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEndpoint extends WebSocketAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(EventEndpoint.class);
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    private Schafkopf schafkopf;
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        LOG.debug("Endpoint connected: {}", sess);
        System.out.println("Endpoint connected:" + sess);

        schafkopf = new Schafkopf(getSession());
        try {
            schafkopf.initializeCardDeck();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        LOG.debug("Received TEXT message: {}", message);
        System.out.println("Received TEXT message:" + message);

        if (message.toLowerCase(Locale.US).contains("bye"))
        {
            getSession().close(StatusCode.NORMAL, "Thanks");
        }

        if (message.toLowerCase(Locale.US).contains("startsimulation"))
        {
            try {
                Gson gson = new Gson();
                String jsonData = gson.toJson("Start");
                getRemote().sendString(jsonData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (message.toLowerCase(Locale.US).contains("stopsimulation"))
        {
            try {
                Gson gson = new Gson();
                String jsonData = gson.toJson("Stop");
                getRemote().sendString(jsonData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);
        LOG.debug("Socket Closed: [{}] {}", statusCode, reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException
    {
        LOG.debug("Awaiting closure from remote");
        closureLatch.await();
    }
}