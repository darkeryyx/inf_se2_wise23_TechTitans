package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import group.artifact.controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@AnonymousAllowed
@CssImport("./css/LoginView.css")
public class LoginView extends Composite<Component> {

    @Autowired
    private UserController userController;

    protected Component initContent() {
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Passwort");
        Anchor pwVergessen = new Anchor("forgotPW", "Passwort vergessen?");
        Anchor account = new Anchor("register", "Account erstellen");

        password.addKeyDownListener(Key.ENTER, event -> {
            login(email.getValue(), password.getValue());
        });

        email.addKeyDownListener(Key.ENTER, event -> {
            login(email.getValue(), password.getValue());
        });

        Button submit = new Button("Bestätigen", event -> login(
                email.getValue(),
                password.getValue()));
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout loginFormLayout = new VerticalLayout(
                new H2("Login"),
                email,
                password,
                submit);

        loginFormLayout.add(pwVergessen);
        loginFormLayout.add(account);

        loginFormLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout jobAdvertisementsLayout = new VerticalLayout();
        Image image = new Image("images/image.png", "successfull job search");
        jobAdvertisementsLayout.add(image);
        jobAdvertisementsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout mainLayout = new HorizontalLayout(jobAdvertisementsLayout, loginFormLayout);
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return mainLayout;
    }

    private void login(String email, String password) {
        if (email.trim().isEmpty()) {
            Notification.show("Bitte E-Mail eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (password.trim().isEmpty()) {
            Notification.show("Bitte Passwort eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            if (userController.login(email, password)) {
                getUI().ifPresent(ui -> ui.access(() -> {
                    ui.navigate("/home");
                    Notification.show("Login erfolgreich!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }));
            } else {
                Notification.show("Falsche Email oder Passwort!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

}