package org.schafkopf;

import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;

/**
 * Creater to make new Instances of the FrontendConnection.
 */
public class FrontendEndpointCreator implements JettyWebSocketCreator {
  private BackendServer backendServer;
  private final MessageListener messageListener;

  public FrontendEndpointCreator(BackendServer backendServer, MessageListener messageListener) {
    this.messageListener = messageListener;
    this.backendServer = backendServer;
  }

  @Override
  public Object createWebSocket(
      JettyServerUpgradeRequest jettyServerUpgradeRequest,
      JettyServerUpgradeResponse jettyServerUpgradeResponse) {
    return new FrontendEndpoint(this.backendServer, messageListener);
  }
}
