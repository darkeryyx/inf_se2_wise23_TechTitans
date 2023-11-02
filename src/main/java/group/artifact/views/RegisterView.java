package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends Composite {

    protected Component initContent(){
        EmailField email = new EmailField("E-Mail");
        TextField vorname = new TextField("Vorname");
        TextField nachname = new TextField("Nachname");
        PasswordField passwort = new PasswordField("Passwort");
        PasswordField passwort2 = new PasswordField("Passwort bestätigen");
        return new VerticalLayout(
                new H2("Register"),
                email,
                vorname,
                nachname,
                passwort,
                passwort2,
                new Button("Bestätigen", event -> register(
                        email.getValue(),
                        vorname.getValue(),
                        nachname.getValue(),
                        passwort.getValue(),
                        passwort2.getValue()
                ))

        );
    }
    private void register(String email, String vorname, String nachname, String passwort, String passwort2){
        //hier passiert die register Logik
    }
}
