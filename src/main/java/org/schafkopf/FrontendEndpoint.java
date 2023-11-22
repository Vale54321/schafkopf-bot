package org.schafkopf;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * Class that represents one Frontend Connection.
 */
public class FrontendEndpoint extends WebSocketAdapter {
  private final CountDownLatch closureLatch = new CountDownLatch(1);
  private BackendServer backendServer;

  public FrontendEndpoint(BackendServer backendServer) {
    this.backendServer = backendServer;
    System.out.println("new FrontendEndpoint");
  }

  @Override
  public void onWebSocketConnect(Session session) {
    super.onWebSocketConnect(session);
    String clientIp = session.getRemoteAddress().toString();
    System.out.println("Endpoint connected from ip: " + clientIp);

    backendServer.addFrontendEndpoint(this);
  }

  @Override
  public void onWebSocketText(String message) {
    super.onWebSocketText(message);
    System.out.println("Received TEXT message:" + message);

    if (message.contains("startsimulation")) {
      backendServer.startSchafkopfGame();
    }

    if (message.contains("stopsimulation")) {
      backendServer.stopSchafkopfGame();
    }

    if (message.contains("showtrumpf")) {
      backendServer.showTrumpf();
    }

    if (message.contains("showfarben")) {
      backendServer.showFarbe();
    }

    if (message.contains("testhand")) {
      backendServer.testHand();
    }

    if (message.contains("setgame")) {
      backendServer.setGame(message);
    }
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);

    backendServer.removeFrontendEndpoint(this);

    System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    closureLatch.countDown();
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace(System.err);
  }

  /**
   * send a Message to the connected FrontEnd.
   */
  public void sendMessage(String message) {
    try {
      getRemote().sendString(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
