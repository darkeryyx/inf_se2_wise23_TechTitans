package group.artifact;

import org.springframework.web.filter.OncePerRequestFilter;

import group.artifact.controller.SessionController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class SidAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    SessionController sessionController;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie sessionCookie = getCookie(request, "sid");
        if (sessionCookie != null && sessionController.validateSessionCookie(sessionCookie)) {
            // valid sid -> set token in security contect to apply roles
            PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken("user", null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * get the cookie with the given name from the request
     * 
     * @param request: client request
     * @param name:    name of the cookie
     * @return: cookie
     */
    private Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null; // cookie not found
    }
}
