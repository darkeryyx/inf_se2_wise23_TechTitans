package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group.artifact.entities.User;
import group.artifact.services.UserService;

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

    public void login(String name, String surname, String passwort) {
        userService.authentificate(name, surname, passwort);
        //todo: if authentificate = true: setSessionCookie(Sid)
    }

    public void logout(){
      //todo 
       //Cookie s = userService.revokeCookie(cookie);    
        //VaadinService.getCurrentResponse().addCookie(s); 
    }
}
