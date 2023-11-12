package group.artifact.controller;
import group.artifact.services.SessionService;
import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionController {

    @Autowired
    SessionService sessionService;

    public boolean validateSessionCookie(Cookie sessionCookie){
        String sid = sessionCookie.getValue();
        return sessionService.validateSession(sid);
    }
}
