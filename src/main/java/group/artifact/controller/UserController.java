package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import group.artifact.entities.User;
import group.artifact.services.UserService;

public class UserController {

    @Autowired
    UserService service;

    public void register( User newUser) {
        service.createUser(newUser);
    }

    @PostMapping("/login")
    public void login(User user) {
        // leeets gooo
    }
}
