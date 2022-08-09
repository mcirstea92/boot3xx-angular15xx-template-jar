package ro.mariuscirstea.eventtracker.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class ErrorHandlerController implements ErrorController {

    private static final String BASE_HREF = "EventTracker";

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("Error for request: {}", request.getPathInfo());
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            log.info("Error with status detected: {}", statusCode);
        }
        response.sendRedirect("/" + BASE_HREF + "/index.html");
        // return js that logs the error and redirects after 5 seconds
        return "Error handled with success";
    }

}
