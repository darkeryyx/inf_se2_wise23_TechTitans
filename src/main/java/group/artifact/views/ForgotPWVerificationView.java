package group.artifact.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import group.artifact.controller.EmailController;
import group.artifact.controller.UserController;
import group.artifact.entities.User;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Route("forgotPW/verify")
@AnonymousAllowed
@CssImport("./css/RegisterVerificationView.css")
public class ForgotPWVerificationView extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    EmailController emailController;
    @Autowired
    UserController userController;

    private String email;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> queryParams = event.getLocation().getQueryParameters().getParameters();
        if (queryParams.containsKey("email")) {
            List<String> emails = queryParams.get("email");
            if (!emails.isEmpty()) {
                this.email = emails.get(0); // Get the first email parameter
            }
        }
    }

    public ForgotPWVerificationView() {
        addClassName("register-verification-view");

        H1 title = new H1("Account entsperren");
        add(title);

        Paragraph text = new Paragraph("Bitte geben Sie den Code ein, den Sie per E-Mail erhalten haben.");
        add(text);
        Button resendButton = new Button("Erneut senden", e -> {
            User user = userController.getUserByEmail(email);
            userController.generateVerificationCode(user);
            emailController.unlockAccountEmail(user.getEmail(), user.getSurname());
            Notification.show("E-Mail erfolgreich versendet").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        resendButton.addClassName("link-button");
        add(resendButton);

        HorizontalLayout inputFields = new HorizontalLayout();
        TextField[] codeFields = new TextField[5];
        for (int i = 0; i < codeFields.length; i++) {
            TextField field = new TextField();
            field.addClassName("code-field");
            field.setMaxLength(1); // allow only one char
            field.setWidth("15vw");
            field.setHeight("15vh");
            codeFields[i] = field; // add each field to the array
            inputFields.add(field); // add each field to the horizontal layout
        }
        add(inputFields); // add inputs to the main layout

        Button verifyButton = new Button("Bestätigen", e -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                // create an array to store the input values
                String pin = "";

                // iterate through the TextField array and retrieve the values
                for (int i = 0; i < codeFields.length; i++) {
                    TextField field = codeFields[i];
                    String fieldValue = field.getValue(); // get the value from the TextField
                    pin += fieldValue; // add the value to the pin string
                }
                User user = userController.getUserByEmail(email);
                if (userController.verifyEmail(user, pin)) {
                    userController.unlock(user.getEmail());
                    ui.navigate("/forgotPW");
                    Notification.show("Account erfolgreich entsperrt")
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification.show("Ihr Account konnte nicht entsperrt werden!")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }));
        });
        verifyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(verifyButton);
        add(buttons);
    }
}
