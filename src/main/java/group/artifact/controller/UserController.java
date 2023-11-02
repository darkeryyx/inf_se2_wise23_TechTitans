package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import group.artifact.entities.User;
import group.artifact.services.UserService;

@RestController
public class UserController {

    @Autowired
    UserService service;
    
    @PostMapping("/register")
    public void register(@RequestBody User newUser){
        
    }

}

