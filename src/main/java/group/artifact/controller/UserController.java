package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void login(String email, String passwort) {
       if(userService.authentificate(email, passwort)){
            Cookie s = userService.setSessionCookie();
            //todo: tie cookie to user in db
            VaadinService.getCurrentResponse().addCookie(s);
        }
       }
    

    public void logout(){ //funktioniert nicht
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName() == "sid"){
                userService.revokeCookie(cookie); 
            }
        }
       //todo: delete cookie in db  
    }

}