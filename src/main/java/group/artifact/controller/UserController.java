package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

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
            Cookie s = userService.setSessionCookie(email);
            //todo: tie cookie to user in db
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            VaadinService.getCurrentResponse().addCookie(s);
            
        }//else do some error handling
       }
    

    public void logout(){ 
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        String name = "sid";
        for(Cookie cookie : cookies){
            if(name.equals(cookie.getName())){
                userService.revokeCookie(cookie); 
                VaadinService.getCurrentResponse().addCookie(cookie);
            }
        }
       //todo: delete cookie in db  
    }

}