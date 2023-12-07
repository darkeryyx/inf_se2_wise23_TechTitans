package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group.artifact.entities.User;
import group.artifact.services.EmailService;
import group.artifact.services.UserService;

@Component
public class EmailController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    public void sendVerificationEmail(String to, String surname) {
        User user = userService.getUserByEmail(to);
        String pin = user.getPin();

        String address = to;
        String title = "Sicherheitsmaßnahme Techtitans - Bestätigen Sie ihre E-Mail!";
        String text = "Sehr geehrte/r Frau/Herr " + surname
                + "\n\nbitte bestätigen Sie Ihre E-Mail-Adresse mit dem folgenden Code:\n\n" + pin
                + "\n\nViele Grüße,\nIhr TechTitans-Team";
        emailService.sendEmail(address, title, text);
    }

    public void unlockAccountEmail(String to, String surname) {
        User user = userService.getUserByEmail(to);
        String pin = user.getPin();

        String address = to;
        String title = "Sicherheitsmaßnahme Techtitans - Entsperren Sie ihren Account!";
        String text = "Sehr geehrte/r Frau/Herr " + surname
                + "\n\nbitte entsperren Sie Ihren Account mit dem folgenden Code:\n\n" + pin
                + "\n\nViele Grüße,\nIhr TechTitans-Team";
        emailService.sendEmail(address, title, text);
    }
}
