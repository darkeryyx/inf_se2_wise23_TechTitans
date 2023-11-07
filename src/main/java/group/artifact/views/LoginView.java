package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import group.artifact.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends Composite<Component> {

    @Autowired
    private UserController userController;

    protected Component initContent() {
        TextField firstName = new TextField("Vorname");
        TextField lastName = new TextField("Nachname");
        PasswordField password = new PasswordField("Passwort");

        VerticalLayout loginFormLayout = new VerticalLayout(
                new H2("Login"),
                firstName,
                lastName,
                password,
                new Button("Bestätigen", event -> login(
                        firstName.getValue(),
                        lastName.getValue(),
                        password.getValue()
                ))
        );

        loginFormLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout jobAdvertisementsLayout = new VerticalLayout();
        jobAdvertisementsLayout.setWidth("400px");


        HorizontalLayout mainLayout = new HorizontalLayout(jobAdvertisementsLayout, loginFormLayout);
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return mainLayout;
    }

    private void login(String firstName, String lastName, String password) {
        if (firstName.trim().isEmpty()) {
            Notification.show("Bitte eine Vornamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }  else if (lastName.trim().isEmpty()) {
            Notification.show("Bitte einen Nachnamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (password.trim().isEmpty()) {
            Notification.show("Bitte ein Passwort eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }  else {
            userController.login(firstName, lastName, password);
        }
    }

}