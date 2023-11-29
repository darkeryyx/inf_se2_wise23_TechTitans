package group.artifact.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.VaadinService;

import group.artifact.dtos.UserDTO;
import group.artifact.entities.User;
import group.artifact.services.UserService;

@Component
public class UserController {

    @Autowired
    UserService userService;

    public String register(UserDTO newUser) {
        return userService.createUser(newUser);
    }

    public void lock(String email){
        userService.lock(email);
    }

    public boolean getLocked(String email){
        return userService.getLocked(email);
    }
    public boolean login(String email, String passwort) {
        if (userService.authenticate(email, passwort)) {
            Cookie cookie = userService.setSessionCookie(email);
            //VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            //samesite funktioniert seit downgrade nicht, häßlicher workaround
            //VaadinService.getCurrentResponse().addCookie(cookie);
            VaadinService.getCurrentResponse().setHeader("Set-Cookie", ""+ cookie.getName() +"="+ cookie.getValue() +";Max-Age=1200;HttpOnly; SameSite=lax");
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

    public boolean checkSQA(String frage, String antwort, String email) {
        return userService.checkSQA(frage, antwort, email);
    }

    public void pwNew(String email, String pw) {
        userService.pwNew(email, pw);
    }

    public boolean pwLengthValid(String pw){
        return userService.pwLengthValid(pw);
    }
    public boolean pwUpperCaseValid(String pw){
        return userService.pwUpperCaseValid(pw);
    }
    public boolean pwSpecialCharValid(String pw){
        return userService.pwSpecialCharValid(pw);
    }
    public boolean pwNumberValid(String pw){
        return userService.pwNumberValid(pw);
    }

    public User getCurrentUser() {
        return userService.getCurrentUser(VaadinService.getCurrentRequest().getCookies());
    }
}