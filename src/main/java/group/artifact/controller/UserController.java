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
        //Test Cookie zu setzen da login noch nicht fertig
        //Cookie s = userService.setSessionCookie("123");    
        //VaadinService.getCurrentResponse().addCookie(s);


    }

    public void login(String surname, String name, String passwort) {
        userService.authentificate(surname, name, passwort);
        //todo: if authentificate = true: setSessionCookie(Sid)
    }

    public void logout(){
      //todo 
       //Cookie s = userService.revokeCookie(cookie);    
        //VaadinService.getCurrentResponse().addCookie(s); 
    }
}
