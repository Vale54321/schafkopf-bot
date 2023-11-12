package org.example;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;

public class FrontendEndpointCreator implements JettyWebSocketCreator
{
    private BackendServer backendServer;
    public FrontendEndpointCreator(BackendServer backendServer) {
        this.backendServer = backendServer;
    }

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest jettyServerUpgradeRequest, JettyServerUpgradeResponse jettyServerUpgradeResponse)
    {
        return new FrontendEndpoint(this.backendServer);
    }
}