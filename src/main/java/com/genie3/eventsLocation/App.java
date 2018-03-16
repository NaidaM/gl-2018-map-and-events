package com.genie3.eventsLocation;

import com.genie3.eventsLocation.exception.ApplicationExceptionMapper;
import com.genie3.eventsLocation.exception.ValidationExceptionMapper;
import com.genie3.eventsLocation.filters.AuthentificationFilter;
import com.genie3.eventsLocation.filters.CORSFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
//import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    public static void main(String[] args) throws Exception {
        // Initialize the server
        Server server = new Server();

        // Add a connector
        ServerConnector connector = new ServerConnector(server);

        connector.setHost("0.0.0.0");
        connector.setPort(8080);
        connector.setIdleTimeout(30000);
        server.addConnector(connector);

        // Configure Jersey
        ResourceConfig rc = new ResourceConfig();
        rc.packages(true, "com.genie3.eventsLocation.ws");

        rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        rc.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

        rc.register(JacksonFeature.class);
       // rc.register(LoggingFilter.class);
          rc.register(ApplicationExceptionMapper.class);
         rc.register(ValidationExceptionMapper.class);
         rc.register(ValidationFeature.class);
         rc.register(MultiPartFeature.class);
         rc.register(AuthentificationFilter.class);
         rc.register(CORSFilter.class);

        // Add a servlet handler for web services (/ws/*)
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(rc));
        ServletContextHandler handlerWebServices = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handlerWebServices.setContextPath("/api/v1");
        handlerWebServices.addServlet(servletHolder, "/*");

        // Add a handler for resources (/*)
        ResourceHandler handlerPortal = new ResourceHandler();
        handlerPortal.setResourceBase("src/main/webapp");
        handlerPortal.setDirectoriesListed(false);
        handlerPortal.setWelcomeFiles(new String[] { "home.html" });
        ContextHandler handlerPortalCtx = new ContextHandler();
        handlerPortalCtx.setContextPath("/");
        handlerPortalCtx.setHandler(handlerPortal);

        // Activate handlers
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { handlerWebServices, handlerPortalCtx });
        server.setHandler(contexts);

        // Start server
        server.start();



    }
}
