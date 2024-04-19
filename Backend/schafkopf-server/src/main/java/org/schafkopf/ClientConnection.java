package org.schafkopf;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.Player;

/** Class that represents one Frontend Connection. */
public class ClientConnection extends WebSocketAdapter implements MessageSender {
  private final CountDownLatch closureLatch = new CountDownLatch(1);
  private DedicatedServer dedicatedServer;

  private Session session;

  public ClientConnection(DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
    System.out.println("new ClientConnection created.");
  }

  @Override
  public void onWebSocketConnect(Session session) {
    this.session = session;
    super.onWebSocketConnect(session);
    String clientIp = session.getRemoteAddress().toString();
    System.out.println("Endpoint connected from ip: " + clientIp);

    dedicatedServer.addFrontendEndpoint(this);
  }

  @Override
  public void onWebSocketText(String message) {
    super.onWebSocketText(message);
    if (message.equals("HEARTBEAT SYN")) {
      System.out.println("Received HEARTBEAT message from " + session.getRemoteAddress() + ".");
      sendMessage("HEARTBEAT ACK");
      return;
    }
    if (message.equals("START_GAME")) {
      System.out.println("Received START_GAME message from " + session.getRemoteAddress() + ".");
      dedicatedServer.addGameSession(new GameSession(new Schafkopf(new Player[] {
        new BotPlayer(), new BotPlayer(), new BotPlayer(), new BotPlayer()
      }, this)));
      return;
    }
    System.out.println("Received TEXT message:" + message);
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);

    dedicatedServer.removeFrontendEndpoint(this);

    System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    closureLatch.countDown();
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace(System.err);
  }

  /** send a Message to the connected FrontEnd. */
  @Override
  public void sendMessage(String message) {
    try {
      getRemote().sendString(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
