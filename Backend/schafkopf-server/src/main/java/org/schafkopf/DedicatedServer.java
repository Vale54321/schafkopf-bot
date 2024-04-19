package org.schafkopf;

import jakarta.servlet.DispatcherType;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;

/** Main Class that represents the Backend Server. */
public class DedicatedServer {
  private final Server server;
  private final ServerConnector connector;

  private final List<ClientConnection> clientConnections = new ArrayList<>();

  private final List<GameSession> gameSessions = new ArrayList<>();

  /** Creates an Instance of the Backend Server. */
  public DedicatedServer() {
    server = new Server();
    InetSocketAddress address = new InetSocketAddress("localhost", 8085);
    connector = new ServerConnector(server);
    connector.setHost(address.getHostName());
    connector.setPort(address.getPort());
    server.addConnector(connector);

    // Setup the basic application "context" for this application at "/"
    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    // Configure CORS settings
    configureCors(context);
    // Configure specific websocket behavior
    JettyWebSocketServletContainerInitializer.configure(
        context,
        (servletContext, wsContainer) -> {
          // Configure default max size
          wsContainer.setMaxTextMessageSize(65535);
          wsContainer.setIdleTimeout(Duration.ofDays(300000));
          // Add websockets
          wsContainer.addMapping("/*", new FrontendEndpointCreator(this));
        });
  }

  /** The main entrypoint of the Application. */
  public static void main(String[] args) throws Exception {
    DedicatedServer server = new DedicatedServer();
    server.start();
    server.join();
  }

  private void configureCors(ServletContextHandler context) {
    // Enable CORS for all paths
    FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", null);

    // Configure allowed origins, headers, and methods
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,OPTIONS");

    // Add filter mappings
    EnumSet<DispatcherType> types = EnumSet.of(DispatcherType.REQUEST);
    context.addFilter(cors, "*", types);
  }

  private void start() throws Exception {
    server.start();
  }

  private void join() throws InterruptedException {
    server.join();
  }

  public void addFrontendEndpoint(ClientConnection endpoint) {
    clientConnections.add(endpoint);
  }

  public void removeFrontendEndpoint(ClientConnection endpoint) {
    clientConnections.remove(endpoint);
  }


  public void addGameSession(GameSession gameSession) {
    gameSessions.add(gameSession);
  }
}
