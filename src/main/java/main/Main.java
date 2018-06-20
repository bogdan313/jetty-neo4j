package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

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
