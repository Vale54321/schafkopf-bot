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
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageOrigin;

/**
 * Main Class that represents the Backend Server.
 */
@WebSocket
public class DedicatedServerConnection implements MessageSender {

  private final MessageListener messageListener;
  private final CountDownLatch closeLatch;
  private final CountDownLatch connectionLatch;
  private static Session session;

  /**
   * Class that represents one Frontend Connection.
   */
  public DedicatedServerConnection(String address, MessageListener messageListener) {
    this.messageListener = messageListener;
    this.closeLatch = new CountDownLatch(1);
    this.connectionLatch = new CountDownLatch(1);

    connect("ws://" + address);
    try {
      connectionLatch.await(); // Wait until the connection is established
    } catch (InterruptedException e) {
      System.err.println("Error waiting for connection: " + e.getMessage());
    }
  }

  /**
   * Class that represents one Frontend Connection.
   */
  @OnWebSocketConnect
  public void onConnect(Session session) {
    this.session = session;
    connectionLatch.countDown();
  }

  @OnWebSocketMessage
  public void onMessage(String message) {
    messageListener.receiveMessage(message);
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

  /**
   * Main Class that represents the Backend Server.
   */
  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    try {
      session.getRemote().sendString(
          new SchafkopfMessage(SchafkopfMessageOrigin.BACKEND, message).getMessageAsString());
    } catch (Exception e) {
      System.err.println("Error sending message: " + e.getMessage());
    }
  }

  public void awaitClose() throws InterruptedException {
    closeLatch.await();
  }

  /**
   * Main Class that represents the Backend Server.
   */
  public void connect(String serverUri) {
    Thread connectionThread = new Thread(() -> {
      try {
        WebSocketClient client = new WebSocketClient();
        try {
          client.start();
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
  }
}
