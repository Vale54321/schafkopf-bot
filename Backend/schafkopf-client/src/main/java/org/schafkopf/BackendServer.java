package org.schafkopf;

import jakarta.servlet.DispatcherType;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;

/**
 * Main Class that represents the Backend Server.
 */
public class BackendServer implements MessageSender {

  private final Server server;

  private final List<FrontendEndpoint> frontendEndpoints = new ArrayList<>();

  /**
   * Creates an Instance of the Backend Server.
   */
  public BackendServer(String hostName, int port, boolean openFrontend,
      MessageListener messageListener) throws Exception {
    server = new Server();

    ServerConnector connector = new ServerConnector(server);
    connector.setHost(hostName);
    connector.setPort(port);
    server.addConnector(connector);

    // Setup the basic application "context" for this application at "/"
    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    // Configure CORS settings
    configureCors(context);

    if (openFrontend) {
      URL webContentUrl = getClass().getClassLoader().getResource("web-content");
      if (webContentUrl == null) {
        throw new RuntimeException("Unable to find 'web-content' directory");
      }
      String webContentPath = webContentUrl.toExternalForm();
      context.setResourceBase(webContentPath);
      context.addServlet(new ServletHolder("frontend", DefaultServlet.class), "/");
    }

    // Configure specific websocket behavior
    JettyWebSocketServletContainerInitializer.configure(
        context,
        (servletContext, wsContainer) -> {
          // Configure default max size
          wsContainer.setMaxTextMessageSize(65535);
          wsContainer.setIdleTimeout(Duration.ofDays(300000));
          // Add websockets
          wsContainer.addMapping("/schafkopf-events/*",
              new FrontendEndpointCreator(this, messageListener));
        });

    if (openFrontend) {
      URI uri = new URI("http://" + hostName + ":" + port); // Replace with your target URL
      Desktop.getDesktop().browse(uri);
    }

    // Start the server in a separate thread
    Thread serverThread = new Thread(() -> {
      try {
        server.start();
        server.join(); // Wait for server to finish execution
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    serverThread.start();
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

  public void addFrontendEndpoint(FrontendEndpoint endpoint) {
    frontendEndpoints.add(endpoint);
  }

  public void removeFrontendEndpoint(FrontendEndpoint endpoint) {
    frontendEndpoints.remove(endpoint);
  }

  /**
   * Sends Message to all Frontend Instances.
   *
   * @param message Message to send (String).
   */
  private void sendMessageToAllFrontendEndpoints(String message) {
    for (FrontendEndpoint endpoint : frontendEndpoints) {
      endpoint.sendMessage(message);
    }
  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    sendMessageToAllFrontendEndpoints(
        message.getBaseMessage().toString());
  }


  public void sendMessageTest(String message) {
    sendMessageToAllFrontendEndpoints(message);
  }
}
