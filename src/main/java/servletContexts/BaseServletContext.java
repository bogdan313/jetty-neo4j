package servletContexts;

import errorMessages.ServletErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * This class provides ability to add your servlets
 */
public abstract class BaseServletContext {
    private final String baseUrl;
    private final ServletContextHandler handler;

    BaseServletContext(String baseUrl) {
        this.baseUrl = baseUrl;
        this.handler = new ServletContextHandler();
        this.handler.setContextPath(baseUrl);
        this.handler.setErrorHandler(new ServletErrorHandler());
        this.addServlets();
    }

    public ServletContextHandler getHandler() {
        return this.handler;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * Via this method you can add your servlets and they are will be connected to Web Server
     *
     * {@code
     * void addServlets() {
     *  ServletContextHandler handler = this.getHandler();
     *  handler.addServlet(new ServletHolder(new SomeServlet()), url);
     * }
     * }
     */
    abstract void addServlets();
}
