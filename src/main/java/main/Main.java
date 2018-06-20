package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/**
 * Start point for Web Server.
 * You can add your servlets with your Servlet Contexts classes, based on {@link servletContexts.BaseServletContext}
 * {@link servletContexts.BaseServletContext#BaseServletContext(String)} requires baseUrl for context and its servlets
 * {@code
 * contextHandlerCollection.setHandlers(new Handler[] {
 *  new HelloContextHandler("/hello"),
 *  ...
 * });
 * }
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

        contextHandlerCollection.setHandlers(new Handler[] {

        });

        server.setHandler(contextHandlerCollection);

        try {
            server.start();
            server.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
