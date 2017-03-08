package com.stanford.nlp;



import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.stanford.nlp.cache.Models;

public class EmbeddedJetty {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJetty.class);

    private static final int PORT = 9290;

    //private static final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION_PACKAGE = "com.stanford.nlp.config";
    private static final String MAPPING_URL = "/api/rest/*";

    public static void main(String[] args) throws Exception {
        new EmbeddedJetty().startJetty(PORT);
    }

    private void startJetty(int port) throws Exception {
        LOGGER.debug("Starting server at port {}", port);
        Server server = new Server(port);

        server.setHandler(getServletContextHandler());

        addRuntimeShutdownHook(server);

        server.start();
        LOGGER.info("Server started at port {}", port);
        // load models
        //Models models = new Models();


        server.join();
    }

    private static ServletContextHandler getServletContextHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        //contextHandler.setContextPath("/");
        // create spring servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(getWebApplicationContext());
        ServletHolder springServletHolder = new ServletHolder("rest-service-dispatcher", dispatcherServlet);

        // add spring servlet to jetty
        contextHandler.addServlet(springServletHolder, MAPPING_URL);
        //context.setResourceBase("src/main/webapp");
        //context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        //context.setParentLoaderPriority(true);
        return contextHandler;
    }

    private static WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION_PACKAGE);
        return context;
    }

    private static void addRuntimeShutdownHook(final Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (server.isStarted()) {
                    server.setStopAtShutdown(true);
                    try {
                        server.stop();
                    } catch (Exception e) {
                        System.out.println("Error while stopping jetty server: " + e.getMessage());
                        LOGGER.error("Error while stopping jetty server: " + e.getMessage(), e);
                    }
                }
            }
        }));
    }

}