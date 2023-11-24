package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group.artifact.services.EmailService;

@Component
public class EmailController {
    
    @Autowired
    EmailService emailService;

    public void sendEmail(String adress, String subject, String text) {
        emailService.sendEmail(adress, subject, text);
    }
}
