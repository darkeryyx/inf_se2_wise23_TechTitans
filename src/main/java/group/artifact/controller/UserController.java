package group.artifact.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.VaadinService;

import group.artifact.entities.User;
import group.artifact.services.UserService;
import jakarta.servlet.http.Cookie;

@Component
public class UserController {

    @Autowired
    UserService userService;

    public void register(User newUser) {
        userService.createUser(newUser);

    }

    public boolean login(String email, String passwort) {
        if (userService.authentificate(email, passwort)) {
            Cookie s = userService.setSessionCookie(email);
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            VaadinService.getCurrentResponse().addCookie(s);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true; // everything worked
        }
        return false;
    }

    public void logout() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        String name = "sid";
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                userService.revokeCookie(cookie);
                VaadinService.getCurrentResponse().addCookie(cookie);
            }
        }
        // todo: delete cookie in db
    }

    public List<String> getQList(String email) {
        return userService.getQuestions(email);
    }

    public boolean checkSQA(String frage, String antwort, String email){
        return userService.checkSQA(frage,antwort,email);
    }

    public void pwNew(String email,String pw){
        userService.pwNew(email,pw);

    }
}