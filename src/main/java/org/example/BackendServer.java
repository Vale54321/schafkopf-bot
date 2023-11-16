package org.example;

import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.EnumSet;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import io.github.cdimascio.dotenv.Dotenv;

public class BackendServer
{
    private List<FrontendEndpoint> frontendEndpoints = new ArrayList<>();
    public static void main(String[] args) throws Exception
    {
        BackendServer server = new BackendServer();
        server.setPort(8080);
        server.start();
        server.join();
    }

    private final Server server;
    private final ServerConnector connector;

    private final Schafkopf schafkopfGame;
    private final KartenLeser nfcLeser;
    private CountDownLatch nfcLatch = new CountDownLatch(1);

    private Boolean readingMode = false;
    private String uidString = "";

    public BackendServer()
    {
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        server = new Server();
        InetSocketAddress address = new InetSocketAddress(dotenv.get("VITE_APP_WEBSOCKET_IP"), 8080);
        connector = new ServerConnector(server);
        connector.setHost(address.getHostName());
        connector.setPort(address.getPort());
        server.addConnector(connector);
        schafkopfGame = new Schafkopf(this);
        nfcLeser = new KartenLeser(this);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Configure CORS settings
        configureCORS(context);

        // Configure specific websocket behavior
        JettyWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
        {
            // Configure default max size
            wsContainer.setMaxTextMessageSize(65535);
            wsContainer.setIdleTimeout(Duration.ofDays(300000));
            // Add websockets
            wsContainer.addMapping("/schafkopf-events/*", new FrontendEndpointCreator(this));

        });
    }

    private void configureCORS(ServletContextHandler context) {
        // Enable CORS for all paths
        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", null);

        // Configure allowed origins, headers, and methods
        cors.setInitParameter("allowedOrigins", "http://192.168.178.126:5173");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,OPTIONS");

        // Add filter mappings
        EnumSet<DispatcherType> types = EnumSet.of(DispatcherType.REQUEST);
        context.addFilter(cors, "*", types);
    }

    public void setPort(int port)
    {
        connector.setPort(port);
    }

    public void start() throws Exception
    {
        server.start();
    }

    public URI getURI()
    {
        return server.getURI();
    }

    public void stop() throws Exception
    {
        server.stop();
    }

    public void join() throws InterruptedException
    {
        server.join();
    }

    public void addFrontendEndpoint(FrontendEndpoint endpoint) {
        frontendEndpoints.add(endpoint);
    }

    public void removeFrontendEndpoint(FrontendEndpoint endpoint) {
        frontendEndpoints.remove(endpoint);
    }

    public void sendMessageToAllFrontendEndpoints(String message) {
        for (FrontendEndpoint endpoint : frontendEndpoints) {
            endpoint.sendMessage(message);
        }
    }

    public void startSchafkopfGame() {
        schafkopfGame.startGame();
    }

    public void stopSchafkopfGame() {
        schafkopfGame.stopGame();
    }

    public void showTrumpf() {
        schafkopfGame.showTrumpf();
    }

    public void showFarbe() {
        schafkopfGame.showFarbe();
    }

    public void testHand() {
        schafkopfGame.testHand();
    }

    public void setGame(String message) {
        schafkopfGame.setGame(message);
    }
    public void nfcGelesen(String uidString) {
        if(this.uidString.equals(uidString)){
            return;
        }
        if(!this.readingMode){
            return;
        }

        this.uidString = uidString;
        nfcLatch.countDown();
    }

    public String waitForCardScan() throws InterruptedException {
        this.readingMode = true;
        nfcLatch.await();
        this.readingMode = false;
        nfcLatch = new CountDownLatch(1);
        return this.uidString;
    }

    public void testHand() {
        schafkopfGame.testHand();
    }

    public void setGame(String message) {
        schafkopfGame.setGame(message);
    }
}