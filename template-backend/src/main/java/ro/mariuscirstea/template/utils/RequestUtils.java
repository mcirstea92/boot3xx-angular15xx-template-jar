package ro.mariuscirstea.template.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ro.mariuscirstea.template.entity.USR_User;
import ro.mariuscirstea.template.exception.TypeNotFoundException;
import ro.mariuscirstea.template.model.RequestDetails;
import ro.mariuscirstea.template.service.UserService;

@Component
public class RequestUtils {

    private final UserService userService;

    public RequestUtils(UserService userService) {
        this.userService = userService;
    }

    public RequestDetails extractReqDetails(HttpServletRequest request) {
        RequestDetails details = new RequestDetails();
        details.setIp(request.getRemoteAddr());
        details.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        long userId = 1L;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String currentPrincipalName = authentication.getName();
                userId = userService.findUserIdByEmail(currentPrincipalName);
            }
        } catch (Exception exception) {
            // probably user is not logged in
        }
        try {
            details.setUser(userService.getUser(userId));
        } catch (TypeNotFoundException tnfe) {
            USR_User dummyUser = new USR_User();
            dummyUser.setId(0L);
            details.setUser(dummyUser);
        }
        return details;
    }

}
