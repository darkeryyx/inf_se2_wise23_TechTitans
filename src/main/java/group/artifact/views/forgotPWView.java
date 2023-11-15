package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import group.artifact.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("forgotPW")
@AnonymousAllowed
public class forgotPWView extends Composite<Component> {

    @Autowired
    private UserController userController;

    protected Component initContent() {
        TextField email = new TextField("Bitte geben Sie Ihre Email ein");

        VerticalLayout enterEmailLayout = new VerticalLayout(
                new H2("Passwort vergessen"),
                email,
                new Button("Bestätigen", event -> checkEmail(
                        email.getValue())));

        return enterEmailLayout;
    }

    private void checkEmail(String email) {
        if (email.trim().isEmpty()) {
            Notification.show("Bitte E-Mail eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            List<String> sqa = userController.getQList(email);
            if (!sqa.isEmpty()) {
                ((VerticalLayout) getContent()).removeAll();
                ((VerticalLayout) getContent()).add(sqaLayout(email));

            } else {
                Notification.show("Ungültige Email").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private VerticalLayout sqaLayout(String email) {
        List<String> sQuestions = userController.getQList(email);
        TextField q1 = new TextField(sQuestions.get(0));
        q1.setWidth("30%");

        VerticalLayout sqaLayout = new VerticalLayout(
                q1,
                new Button("Bestätigen", event -> ((VerticalLayout) getContent()).add(checkSQA(
                        sQuestions.get(0),
                        q1.getValue(),
                        email,
                        sQuestions
                ))));
        return sqaLayout;
    }


    public VerticalLayout checkSQA(String frage, String antwort, String email, List<String> liste) {
        //wenn erste frage korrekt beantwortet wurde, wird die 2. angezeigt
        if (userController.checkSQA(frage, antwort, email)) {
            ((VerticalLayout) getContent()).removeAll();

            TextField q2 = new TextField(liste.get(1));
            q2.setWidth("30%");

            VerticalLayout q2Layout = new VerticalLayout(
                    q2,
                    new Button("Bestätigen", event -> ((VerticalLayout) getContent()).add(pwReset(
                            liste.get(1),
                            q2.getValue(),
                            email
                    ))));


            return q2Layout;

        } else {
            Notification.show("Falsche Antwort").addThemeVariants(NotificationVariant.LUMO_ERROR);
            ((VerticalLayout) getContent()).removeAll();
            return sqaLayout(email);
        }
    }

    public VerticalLayout pwReset(String frage, String antwort, String email) {

        if (userController.checkSQA(frage, antwort, email)) {
            ((VerticalLayout) getContent()).removeAll();

            TextField reset = new TextField("Bitte geben Sie Ihr neues Passwort ein");
            reset.setWidth("30%");

            VerticalLayout pwNeu = new VerticalLayout(
                    reset,
                    new Button("Bestätigen", event -> pwNew(
                            email,
                            reset.getValue()
                    )));

            return pwNeu;

        } else {
            Notification.show("Falsche Antwort").addThemeVariants(NotificationVariant.LUMO_ERROR);
            ((VerticalLayout) getContent()).removeAll();
            return sqaLayout(email);
        }
    }

    public void pwNew(String email, String pw) {
        userController.pwNew(email,pw);
        Notification.show("Passwort erfolgreich zurückgesetzt").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().navigate("login");
    }

}
