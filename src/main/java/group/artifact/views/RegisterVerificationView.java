package group.artifact.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@Route("register/verify")
@RolesAllowed("ROLE_USER")
@CssImport("./css/RegisterVerificationView.css")
public class RegisterVerificationView extends VerticalLayout {

    public RegisterVerificationView() {
        addClassName("register-verification-view");

        HorizontalLayout inputFields = new HorizontalLayout();
        TextField[] codeFields = new TextField[5];
        for (int i = 0; i < codeFields.length; i++) {
            TextField field = new TextField();
            field.addClassName("code-field");
            field.setMaxLength(1); // allow only one char
            field.setWidth("15vw");
            field.setHeight("15vh");
            inputFields.add(field); // add each field to the horizontal layout
        }
        add(inputFields); // add inputs to the main layout

        Anchor resendLink = new Anchor("/", "resend verification code");
        add(resendLink);

        Button verifyButton = new Button("Verify");
        verifyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(verifyButton);
    }
}
