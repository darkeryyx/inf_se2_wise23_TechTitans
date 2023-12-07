package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
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
    private int answer1Attempts = 0;
    private int answer2Attempts = 0;

    protected Component initContent() {
        TextField email = new TextField("Bitte geben Sie Ihre E-Mail ein");
        email.setWidth("30%");

        email.addKeyDownListener(Key.ENTER, event -> {
            checkEmail(
                    email.getValue());
        });
        VerticalLayout enterEmailLayout = new VerticalLayout();
        enterEmailLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        enterEmailLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        enterEmailLayout.getStyle().set("display", "flex");
        enterEmailLayout.setSizeFull();

        enterEmailLayout.add(new H2("Passwort vergessen"), email,
                new Button("Bestätigen", event -> checkEmail(email.getValue())));

        return enterEmailLayout;
    }

    private void checkEmail(String email) {
        if (email.trim().isEmpty()) {
            Notification.show("Bitte E-Mail eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (userController.getLocked(email)) {
            getUI().ifPresent(ui -> ui.navigate("forgotPW/verify?email=" + email));
        } else {
            List<String> sqa = userController.getQList(email);
            if (!sqa.isEmpty()) {
                ((VerticalLayout) getContent()).removeAll();
                ((VerticalLayout) getContent()).add(sqaLayout(email));

            }
        }
    }

    private VerticalLayout sqaLayout(String email) {
        List<String> sQuestions = userController.getQList(email);
        TextField q1 = new TextField(sQuestions.get(0));
        q1.setWidth("40%");

        q1.addKeyDownListener(Key.ENTER, event -> {
            ((VerticalLayout) getContent()).add(checkSQA(sQuestions.get(0),
                    q1.getValue(),
                    email,
                    sQuestions));
        });

        VerticalLayout sqaLayout = new VerticalLayout();
        sqaLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        sqaLayout.setSizeFull();

        sqaLayout.add(new H5("Sie haben jeweils nur 3 Versuche, um die Sicherheitsfragen korrekt zu beantworten!"),
                q1,
                new Button("Bestätigen", event -> ((VerticalLayout) getContent()).add(checkSQA(
                        sQuestions.get(0),
                        q1.getValue(),
                        email,
                        sQuestions))));

        return sqaLayout;
    }

    public VerticalLayout checkSQA(String frage, String antwort, String email, List<String> liste) {
        if (userController.checkSQA(frage, antwort, email)) {
            ((VerticalLayout) getContent()).removeAll();

            TextField q2 = new TextField(liste.get(1));
            q2.setWidth("40%");

            q2.addKeyDownListener(Key.ENTER, event -> {
                ((VerticalLayout) getContent()).add(pwReset(
                        liste.get(1),
                        q2.getValue(),
                        email));
            });

            VerticalLayout q2Layout = new VerticalLayout();
            q2Layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            q2Layout.setSizeFull();

            q2Layout.add(q2,
                    new Button("Bestätigen", event -> ((VerticalLayout) getContent()).add(pwReset(
                            liste.get(1),
                            q2.getValue(),
                            email))));

            return q2Layout;
        } else {
            Notification.show(String.format("Falsche Antwort! Noch %d Versuche!", (3 - (++answer1Attempts))))
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);

            if (answer1Attempts == 3) {
                userController.lock(email);
                Notification.show("Account gesperrt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
            ((VerticalLayout) getContent()).removeAll();
            return sqaLayout(email);
        }
    }

    public VerticalLayout pwReset(String frage, String antwort, String email) {

        if (userController.checkSQA(frage, antwort, email)) {
            ((VerticalLayout) getContent()).removeAll();

            PasswordField reset = new PasswordField("Bitte geben Sie Ihr neues Passwort ein");
            PasswordField confirm = new PasswordField("Bestätigen Sie das Passwort");
            reset.setWidth("20%");
            confirm.setWidth("20%");

            confirm.addKeyDownListener(Key.ENTER, event -> {
                pwNew(
                        email,
                        reset.getValue(),
                        confirm.getValue());
            });

            VerticalLayout pwNeu = new VerticalLayout();
            pwNeu.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            pwNeu.setSizeFull();

            pwNeu.add(
                    reset,
                    confirm,
                    new Button("Bestätigen", event -> pwNew(
                            email,
                            reset.getValue(),
                            confirm.getValue())));

            return pwNeu;

        } else {
            Notification.show(String.format("Falsche Antwort! Noch %d Versuche!", (3 - (++answer2Attempts))))
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);

            if (answer2Attempts == 3) {
                userController.lock(email);
                Notification.show("Account gesperrt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
            ((VerticalLayout) getContent()).removeAll();
            return sqaLayout(email);
        }
    }

    public void pwNew(String email, String pw, String pw2) {
        if (pw.trim().isEmpty() || pw2.trim().isEmpty()) {
            Notification.show("Bitte ein Passwort eingeben!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (!pw.equals(pw2)) {
            Notification.show("Passwörter stimmen nicht überein").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            userController.pwNew(email, pw);
            Notification.show("Passwort erfolgreich zurückgesetzt").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate("login");
        }
    }

}
