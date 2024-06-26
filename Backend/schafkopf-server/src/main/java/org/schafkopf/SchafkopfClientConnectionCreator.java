package org.schafkopf;

import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;

/**
 * Creater to make new Instances of the FrontendConnection.
 */
public class SchafkopfClientConnectionCreator implements JettyWebSocketCreator {

  private DedicatedServer dedicatedServer;

  public SchafkopfClientConnectionCreator(DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
  }

  @Override
  public Object createWebSocket(
      JettyServerUpgradeRequest jettyServerUpgradeRequest,
      JettyServerUpgradeResponse jettyServerUpgradeResponse) {
    return new SchafkopfClientConnection(this.dedicatedServer);
  }
}
