package errorMessages;

import helpers.Constants;
import helpers.GsonHelper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletErrorHandler extends ErrorHandler {
    @Override
    public void handle(String target, Request baseRequest,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Constants.CONTENT_TYPE_JSON);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println(new GsonHelper(new ErrorMessage(ErrorMessage.SERVER_ERROR)));
    }
}
