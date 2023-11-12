package group.artifact.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import group.artifact.entities.User;
import group.artifact.controller.UserController;

@Route("register")
@AnonymousAllowed
public class RegisterView extends Composite<Component> {
    @Autowired
    private UserController userController;

    protected Component initContent() {
        EmailField email = new EmailField("E-Mail");
        email.setPattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        TextField vorname = new TextField("Vorname");
        TextField nachname = new TextField("Nachname");
        PasswordField passwort = new PasswordField("Passwort");
        PasswordField passwort2 = new PasswordField("Passwort bestätigen");
        Checkbox checkbox = new Checkbox("Ich stimme den AGBs zu");
        VerticalLayout layout = new VerticalLayout(
                new H2("Registrieren"),
                email,
                vorname,
                nachname,
                passwort,
                passwort2,
                checkbox,
                new Button("Bestätigen", event -> register(
                        email,
                        vorname.getValue(),
                        nachname.getValue(),
                        passwort.getValue(),
                        passwort2.getValue(),
                        checkbox.getValue().toString()
                ))

        );
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void register(EmailField email, String vorname, String nachname, String passwort, String passwort2, String checkBox) {
        if (email.getValue().trim().isEmpty() || !email.getValue().matches(email.getPattern())) {
            Notification.show("Bitte eine gültige Email eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (vorname.trim().isEmpty()) {
            Notification.show("Bitte einen Vornamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (nachname.trim().isEmpty()) {
            Notification.show("Bitte einen Nachnamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (!passwort.equals(passwort2)) {
            Notification.show("Passwörter stimmen nicht überein").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if(checkBox.equals("false")){
            Notification.show("Bitte stimme unseren AGB zu").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            Notification.show("Registrierung erfolgreich").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            userController.register(new User(vorname, nachname, passwort, email.getValue()));
        }
    }
}
