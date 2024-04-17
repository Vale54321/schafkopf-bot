package org.schafkopf;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.DispatcherType;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.schafkopf.cardreader.UsbCardReader;

/** Main Class that represents the Backend Server. */
public class BackendServer {
  private final Server server;
  private final ServerConnector connector;
  private CountDownLatch nfcLatch = new CountDownLatch(1);
  private Boolean readingMode = false;
  private String uidString = "";

  /** Important variables. */
  public final Schafkopf schafkopfGame;

  private final List<FrontendEndpoint> frontendEndpoints = new ArrayList<>();

  /** Creates an Instance of the Backend Server. */
  public BackendServer() throws URISyntaxException, IOException {
    server = new Server();
    InetSocketAddress address = new InetSocketAddress("localhost", 8080);
    connector = new ServerConnector(server);
    connector.setHost(address.getHostName());
    connector.setPort(address.getPort());
    server.addConnector(connector);

    schafkopfGame = new Schafkopf(this);

    new UsbCardReader(this);

    // Setup the basic application "context" for this application at "/"
    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    // Configure CORS settings
    configureCors(context);

    URL webContentUrl = getClass().getClassLoader().getResource("web-content");
    if (webContentUrl == null) {
      throw new RuntimeException("Unable to find 'web-content' directory");
    }

    String webContentPath = webContentUrl.toExternalForm();
    context.setResourceBase(webContentPath);

    System.out.println("Web Content Path: " + webContentPath);

    // Configure specific websocket behavior
    JettyWebSocketServletContainerInitializer.configure(
        context,
        (servletContext, wsContainer) -> {
          // Configure default max size
          wsContainer.setMaxTextMessageSize(65535);
          wsContainer.setIdleTimeout(Duration.ofDays(300000));
          // Add websockets
          wsContainer.addMapping("/schafkopf-events/*", new FrontendEndpointCreator(this));
        });

    // Integrate simple HTTP server
    startHttpServer();
    URI uri = new URI("http://localhost:8081"); // Replace with your target URL
    Desktop.getDesktop().browse(uri);
  }

  private void startHttpServer() {
    try {
      HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
      httpServer.createContext("/", new MyHandler());
      httpServer.setExecutor(null);
      httpServer.start();
      System.out.println("HTTP Server started on port 8081");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      String path = t.getRequestURI().getPath();
      if ("/".equals(path)) {
        path = "/index.html"; // default to index.html
      }

      try {
        InputStream fileStream =
            getClass().getClassLoader().getResourceAsStream("web-content" + path);
        if (fileStream != null) {
          byte[] data = fileStream.readAllBytes();
          // Set the appropriate MIME type for JavaScript files
          String mimeType = getMimeType(path);
          t.getResponseHeaders().set("Content-Type", mimeType);
          t.sendResponseHeaders(200, data.length);

          try (OutputStream os = t.getResponseBody()) {
            os.write(data);
          }
        } else {
          // File not found
          t.sendResponseHeaders(404, -1);
        }
      } catch (IOException e) {
        e.printStackTrace();
        t.sendResponseHeaders(500, -1);
      }
    }

    private String getMimeType(String path) {
      if (path.endsWith(".js")) {
        return "application/javascript";
      } else if (path.endsWith(".html")) {
        return "text/html";
      } else if (path.endsWith(".css")) {
        return "text/css";
      }
      // Add more MIME types as needed
      return "application/octet-stream";
    }
  }

  /** The main entrypoint of the Application. */
  public static void main(String[] args) throws Exception {
    BackendServer server = new BackendServer();
    server.setPort(8080);
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

  private void setPort(int port) {
    connector.setPort(port);
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
  public void sendMessageToAllFrontendEndpoints(String message) {
    for (FrontendEndpoint endpoint : frontendEndpoints) {
      endpoint.sendMessage(message);
    }
  }

  /**
   * Sends Message to all Frontend Instances.
   *
   * @param message Message to send (JsonObject).
   */
  public void sendMessageToAllFrontendEndpoints(JsonObject message) {
    for (FrontendEndpoint endpoint : frontendEndpoints) {
      endpoint.sendMessage(message.toString());
    }
  }

  /** method to call to wait for NFC input. */
  public String waitForCardScan() throws InterruptedException {
    this.readingMode = true;
    nfcLatch.await();
    Thread.sleep(20);
    this.readingMode = false;
    nfcLatch = new CountDownLatch(1);
    return this.uidString;
  }

  /**
   * checks uid of scanned card and do nothing if Server is not in reading mode.
   *
   * @param uidString uid to check.
   */
  public void nfcGelesen(String uidString) {
    if (this.uidString.equals(uidString)) {
      return;
    }
    if (!this.readingMode) {
      return;
    }

    this.uidString = uidString;
    nfcLatch.countDown();
  }
}
