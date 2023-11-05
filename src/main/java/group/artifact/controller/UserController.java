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
    }

    public void login(String surname, String name, String passwort) {
        userService.authentificate(surname, name, passwort);
    }
}
