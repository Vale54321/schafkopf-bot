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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import io.github.cdimascio.dotenv.Dotenv;

public class BackendServer
{
    private static final Logger LOG = LoggerFactory.getLogger(BackendServer.class);

    public static void main(String[] args) throws Exception
    {
        BackendServer server = new BackendServer();
        server.setPort(8080);
        server.start();
        server.join();
    }

    private final Server server;
    private final ServerConnector connector;

    public BackendServer()
    {
        Dotenv dotenv = Dotenv.configure().load();
        server = new Server();
        InetSocketAddress address = new InetSocketAddress(dotenv.get("VITE_APP_WEBSOCKET_IP"), 8080);
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
        configureCORS(context);

        // Configure specific websocket behavior
        JettyWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
        {
            // Configure default max size
            wsContainer.setMaxTextMessageSize(65535);
            wsContainer.setIdleTimeout(Duration.ofDays(300000));
            // Add websockets
            wsContainer.addMapping("/schafkopf-events/*", new EventEndpointCreator());
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
        LOG.info("Use Ctrl+C to stop server");
        server.join();
    }
}