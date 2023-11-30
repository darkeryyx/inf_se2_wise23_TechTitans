package group.artifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group.artifact.services.EmailService;

@Component
public class EmailController {
    
    @Autowired
    EmailService emailService;

    public void sendVerificationEmail(String to, String surname) {
        String address = to;
        String title = "Sicherheitsmaßnahme Techtitans - Bestätigen Sie ihre E-Mail!";
        String text = "Sehr geehrte/r Frau/Herr " + surname + "\n\nbitte bestätigen Sie Ihre E-Mail-Adresse mit dem folgenden Code:\n\n83923\n\nViele Grüße,\nIhr TechTitans-Team";
        emailService.sendEmail(address, title, text);
    }
}
