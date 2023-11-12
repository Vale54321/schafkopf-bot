package org.example;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class EventClient
{
    public static void main(String[] args)
    {
        EventClient client = new EventClient();
        URI uri = URI.create("ws://localhost:8080/events/");
        try
        {
            client.run(uri);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

    public void run(URI uri) throws Exception
    {
        WebSocketClient client = new WebSocketClient();

        try
        {
            client.start();
            // The socket that receives events
            EventEndpoint socket = new EventEndpoint();
            // Attempt Connect
            Future<Session> fut = client.connect(socket, uri);
            // Wait for Connect
            Session session = fut.get();

            // Send a message
            session.getRemote().sendString("Hello");

            // Send another message
            session.getRemote().sendString("Goodbye");

            // Wait for other side to close
            socket.awaitClosure();

            // Close session
            session.close();
        }
        finally
        {
            client.stop();
        }
    }
}