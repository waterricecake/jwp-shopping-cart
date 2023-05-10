package cart.authorization;

import cart.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EmailPasswordAuthInterceptor extends HandlerInterceptorAdapter {
    private final UserService userService;

    public EmailPasswordAuthInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return userService.authUser(EmailPasswordExtractor.extract(header));
    }
}
