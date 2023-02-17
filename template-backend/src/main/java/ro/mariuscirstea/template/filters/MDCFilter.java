package ro.mariuscirstea.template.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@WebFilter
@Slf4j
public class MDCFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("CorrelationId", correlationId);
        response.setHeader("X-CorrelationId", correlationId);
        filterChain.doFilter(request, response);
    }

}
