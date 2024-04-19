package org.schafkopf;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/** Main Class that represents the Backend Server. */
@WebSocket
public class DedicatedServerConnection {

  private final MessageSender messageSender;
  private final CountDownLatch closeLatch;
  private static Session session;

  public DedicatedServerConnection(MessageSender messageSender) {
    this.messageSender = messageSender;
    this.closeLatch = new CountDownLatch(1);
  }

  @OnWebSocketConnect
  public void onConnect(Session session) {
    this.session = session;
    System.out.println("Connected to server.");
  }

  @OnWebSocketMessage
  public void onMessage(String message) {
    messageSender.sendMessage(message);
    System.out.println("Received message from server: " + message);
  }

  @OnWebSocketClose
  public void onClose(int statusCode, String reason) {
    System.out.println("Connection closed: " + reason);
    closeLatch.countDown();
  }

  @OnWebSocketError
  public void onError(Throwable cause) {
    System.err.println("Error occurred: " + cause.getMessage());
  }

  /** Main Class that represents the Backend Server. */
  public static void sendMessage(String message) {
    try {
      session.getRemote().sendString(message);
      System.out.println("Sent message to server: " + message);
    } catch (Exception e) {
      System.err.println("Error sending message: " + e.getMessage());
    }
  }

  public void awaitClose() throws InterruptedException {
    closeLatch.await();
  }

  /** Main Class that represents the Backend Server. */
  public void connect() {
    Thread connectionThread = new Thread(() -> {
      try {
        String serverUri = "ws://localhost:8085/";
        WebSocketClient client = new WebSocketClient();
        try {
          client.start();
          //DedicatedServerConnection socketClient = new DedicatedServerConnection();
          HeartbeatSender heartbeatSender = new HeartbeatSender(this);
          heartbeatSender.start(); // Start sending heartbeat messages
          URI uri = new URI(serverUri);
          ClientUpgradeRequest request = new ClientUpgradeRequest();
          client.connect(this, uri, request);

          System.out.println("Connecting to : " + uri);
          this.awaitClose();
        } catch (Exception e) {
          System.err.println("Error connecting to server: " + e.getMessage());
        } finally {
          try {
            client.stop();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        System.err.println("Error starting dedicated server connection: " + e.getMessage());
      }
    });
    connectionThread.start();
    start();
  }

  public static void start() {
    sendMessage("START_GAME");
  }
}
