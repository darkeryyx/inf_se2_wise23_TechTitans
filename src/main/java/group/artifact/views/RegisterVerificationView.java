package group.artifact.views;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.Route;

import group.artifact.controller.EmailController;
import group.artifact.controller.UserController;
import group.artifact.entities.User;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

@Route("register/verify")
@RolesAllowed("ROLE_USER")
@CssImport("./css/RegisterVerificationView.css")
public class RegisterVerificationView extends BasisView {
    @Autowired
    EmailController emailController;
    @Autowired
    UserController userController;

    public RegisterVerificationView(){
        super();
        this.setContent(content());
        this.add();
    }
    public VerticalLayout content() {
        //addClassName("register-verification-view");

        H1 title = new H1("E-Mail bestätigen");
        add(title);

        Paragraph text = new Paragraph("Bitte geben Sie den Code ein, den Sie per E-Mail erhalten haben.");
        add(text);
        Button resendButton = new Button("Erneut senden", e -> {
            User user = userController.getCurrentUser();
            emailController.sendVerificationEmail(user.getEmail(), user.getSurname());
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
                if (userController.verifyEmail(userController.getCurrentUser(), pin)) {
                    ui.navigate("/create/profile");
                    Notification.show("E-Mail erfolgreich verifiziert")
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification.show("E-Mail konnte nicht verifiziert werden!")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }));
        });
        verifyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button skipButton = new Button("Überspringen", e -> UI.getCurrent().navigate("/create/profile"));
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(verifyButton, skipButton);
        add(buttons);
        VerticalLayout content = new VerticalLayout(title,text, resendButton,inputFields, buttons);
        return content;
    }

}
