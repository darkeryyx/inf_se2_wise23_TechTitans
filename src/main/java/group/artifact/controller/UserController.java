package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group.artifact.entities.User;
import group.artifact.services.UserService;

@Component
public class UserController {

    @Autowired
    UserService service;

    public void register(User newUser) {
        service.createUser(newUser);
    }

    public void login(String surname, String name, String passwort) {
        service.authentificate(surname, name, passwort);
    }
}
