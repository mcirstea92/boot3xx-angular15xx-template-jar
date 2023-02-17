package ro.mariuscirstea.template.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;
import ro.mariuscirstea.template.utils.FileUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ErrorHandlerController extends ResponseEntityExceptionHandler implements ErrorController {

    private final ObjectMapper objectMapper;

    public ErrorHandlerController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/error")
    @ResponseBody
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        NestedServletException nse = (NestedServletException) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (status != null && nse != null) {
            log.info("Error with status detected: {}. Details: {}", status, nse.getMessage());
        }
        // response.sendRedirect("/" + BASE_HREF + "/index.html");
        // return js that logs the error and redirects after 5 seconds
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status", status);
        errorMap.put("error", "An internal error occurred, please contact the administrator using this code '" + MDC.get("CorrelationId") + "'");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(objectMapper.writeValueAsString(errorMap));
    }

    @RequestMapping(value = "/accessDenied")
    public ResponseEntity<String> handleAccessDenied() {
        String fileContent = FileUtils.getResourceFileAsString("public/error/error-403.json");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(fileContent);
    }

}
