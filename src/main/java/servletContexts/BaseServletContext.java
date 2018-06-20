package servletContexts;

import errorMessages.ServletErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

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

    abstract void addServlets();
}
