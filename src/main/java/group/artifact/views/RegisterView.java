package group.artifact.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import group.artifact.controller.UserController;
import group.artifact.dtos.impl.UserDTOImpl;
import group.artifact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;

@Route("register")
@AnonymousAllowed
@CssImport("./css/RegisterView.css")

public class RegisterView extends BasisView {
    @Autowired
    private UserController userController;
    private Map<String, String> sicherheitsQA = new HashMap<>();
    public RegisterView() {
        super();
        this.setContent(createMain());
        this.add();
    }


    private VerticalLayout createMain(){
        VerticalLayout main = new VerticalLayout();

        //main.setFlexDirection(FlexLayout.FlexDirection.COLUMN);

        final List<String> allSecurityQuestions = Arrays.asList(
                "Wie lautet der Mädchenname Ihrer Mutter?",
                "In welcher Stadt wurden Sie geboren?",
                "Wie lautet der Name Ihres ersten Haustieres?",
                "In welcher Stadt bzw. welchem Ort haben sich Ihre Eltern kennengelernt?",
                "Wie lautet der zweite Vorname Ihrer Oma?",
                "Wie heißt die Stadt, in der Sie sich verlaufen haben?",
                "Wie heißt der Lehrer, der Ihnen Ihre erste 1 gab?");
        Collections.shuffle(allSecurityQuestions);
        List<String> selectedSecurityQuestions = allSecurityQuestions.subList(0, 3);

        EmailField email = new EmailField("E-Mail");
        email.setPattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        email.setRequiredIndicatorVisible(true);
        TextField vorname = new TextField("Vorname");
        vorname.setRequired(true);
        TextField nachname = new TextField("Nachname");
        nachname.setRequired(true);
        PasswordField passwort = new PasswordField("Passwort");
        passwort.setRequired(true);
        PasswordField passwort2 = new PasswordField("Passwort bestätigen");
        passwort2.setRequired(true);

        //passwort anforderungen
        HorizontalLayout passwortAnforderungen =new HorizontalLayout();
        HorizontalLayout laenge= createPasswordRequirement("mind. 8 Zeichen");
        HorizontalLayout großbuchstabe= createPasswordRequirement("mind. 1 Großbuchstabe");
        HorizontalLayout sonderzeichen= createPasswordRequirement("mind. 1 Sonderzeichen");
        HorizontalLayout zahl= createPasswordRequirement("mind. 1 Zahl");

        passwortAnforderungen.add(laenge,großbuchstabe,sonderzeichen,zahl);
        passwort.addValueChangeListener(event ->validatePW(event.getValue(), laenge, großbuchstabe, sonderzeichen,zahl));

        // für die sicherheitsfragen
        MultiSelectComboBox<String> securityQuestionsComboBox = new MultiSelectComboBox<>();
        securityQuestionsComboBox.setLabel("Wählen Sie 2 Sicherheitsfragen");
        securityQuestionsComboBox.setItems(selectedSecurityQuestions);
        securityQuestionsComboBox.setPlaceholder("Sicherheitsfragen auswählen");
        securityQuestionsComboBox.setClearButtonVisible(true);
        securityQuestionsComboBox.setRequired(true);
        securityQuestionsComboBox.setRequiredIndicatorVisible(true);
        securityQuestionsComboBox.setWidth("20rem");
        securityQuestionsComboBox.addValueChangeListener(this::onSecurityQuestionsSelected);
        Checkbox checkbox = new Checkbox("Ich bin mit der Verarbeitung meiner Daten im Rahmen der Datenschutzerklärung einverstanden.");
        Anchor link = new Anchor("/Datenschutz", "Datenschutzerklärung");
        link.setTarget("_blank");

        HorizontalLayout line1 = new HorizontalLayout(vorname, nachname);
        HorizontalLayout line2 = new HorizontalLayout(email);
        email.setWidth("25rem");
        HorizontalLayout line3 = new HorizontalLayout(passwort, passwort2);
        
        Button submit = new Button("Bestätigen", event -> register(
                email,
                vorname.getValue(),
                nachname.getValue(),
                passwort.getValue(),
                passwort2.getValue(),
                checkbox.getValue().toString(),
                sicherheitsQA));
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        main.add(new H2("Registrieren"),
                line1,
                line2,
                line3,
                passwortAnforderungen,
                securityQuestionsComboBox,
                answerLayout,
                checkbox,
                link,
                submit);

        main.setAlignItems(Alignment.CENTER);
        answerLayout.setAlignItems(Alignment.CENTER);
        return main;
    }
    public HorizontalLayout createPasswordRequirement(String text){
        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        icon.setColor("var(--lumo-error-color)");
        icon.setSize("15px");

        HorizontalLayout layout =new HorizontalLayout(icon, new Span(text));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        return layout;
    }
    public void validatePW(String passwort, HorizontalLayout laenge, HorizontalLayout groß, HorizontalLayout sonder, HorizontalLayout zahl){

        updateIndicator(laenge, userController.pwLengthValid(passwort));
        updateIndicator(groß, userController.pwUpperCaseValid(passwort));
        updateIndicator(sonder, userController.pwSpecialCharValid(passwort));
        updateIndicator(zahl, userController.pwNumberValid(passwort));
    }
    public void updateIndicator(HorizontalLayout ind, boolean valid){
        Icon icon= (Icon) ind.getComponentAt(0);
        icon.setColor(valid? "green" : "var(--lumo-error-color)");
    }
    public VerticalLayout answerLayout = new VerticalLayout();
    public void onSecurityQuestionsSelected(
            AbstractField.ComponentValueChangeEvent<MultiSelectComboBox<String>, Set<String>> value) {
        Set<String> selectedQuestions = value.getValue();
        sicherheitsQA.clear();
        if (selectedQuestions.size() == 2) {
            answerLayout.removeAll();
            for (String question : selectedQuestions) {

                TextField answerField = new TextField();
                answerField.setLabel("Antwort auf \"" + question + "\":");
                answerField.setWidth("25rem");
                answerField.addValueChangeListener(e -> sicherheitsQA.put(question, e.getValue()));
                answerLayout.add(answerField);
            }
        } else if (selectedQuestions.size() < 2) {
            answerLayout.removeAll();
        }
    }
    public void register(EmailField email, String vorname, String nachname, String passwort, String passwort2,
                         String checkBox, Map<String, String> sicherheitsQA) {
        if (email.getValue().trim().isEmpty() || !email.getValue().matches(email.getPattern())) {
            Notification.show("Bitte eine gültige E-Mail eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (vorname.trim().isEmpty()) {
            Notification.show("Bitte einen Vornamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (nachname.trim().isEmpty()) {
            Notification.show("Bitte einen Nachnamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (!passwort.equals(passwort2)) {
            Notification.show("Passwörter stimmen nicht überein").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (checkBox.equals("false")) {
            Notification.show("Bitte stimmen Sie unseren AGB zu").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (sicherheitsQA.size() < 2) {
            Notification.show("Bitte beantworten Sie alle Sicherheitsfragen").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }else if(!userController.pwNumberValid(passwort) ||!userController.pwSpecialCharValid(passwort)||!userController.pwUpperCaseValid(passwort) ||!userController.pwLengthValid(passwort)) {
            Notification.show("Passwort entspricht nicht den Anforderungen!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            User user = new User(vorname, nachname, passwort, email.getValue(), false);
            String result = userController
                    .register(new UserDTOImpl(user, sicherheitsQA));
            if (result == "email_error") {
                Notification.show("E-Mail bereits vergeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                userController.login(email.getValue(), passwort);
                getUI().ifPresent(ui -> ui.access(() -> {
                    ui.navigate(RegisterVerificationView.class);
                }));

            }
        }
    }
}
