package group.artifact.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
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

import java.util.*;

@Route("register")
@AnonymousAllowed
public class RegisterView extends Composite<Component> {
    @Autowired
    private UserController userController;
    private Map<String, String> sicherheitsQA = new HashMap<>();

    protected Component initContent() {
        final List<String> allSecurityQuestions = Arrays.asList(
                "Wie lautet der Mädchenname Ihrer Mutter?",
                "In welcher Stadt wurden Sie geboren?",
                "Wie lautet der Name Ihres ersten Haustiers?",
                "In welcher Stadt bzw. welchem Ort haben sich Ihre Eltern kennengelernt?",
                "Wie lautet der zweite Vorname Ihrer Oma?",
                "Wie heißt die Stadt, in der Sie sich verlaufen haben?",
                "Wie heißt der Lehrer, der ihnen Ihre erste 1 gab?");
        Collections.shuffle(allSecurityQuestions);
        List<String> selectedSecurityQuestions = allSecurityQuestions.subList(0, 3);

        EmailField email = new EmailField("E-Mail");
        email.setPattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        TextField vorname = new TextField("Vorname");
        TextField nachname = new TextField("Nachname");
        PasswordField passwort = new PasswordField("Passwort");
        PasswordField passwort2 = new PasswordField("Passwort bestätigen");

        // für die sicherheitsfragen
        MultiSelectComboBox<String> securityQuestionsComboBox = new MultiSelectComboBox<>();
        securityQuestionsComboBox.setLabel("Wählen Sie 2 Sicherheitsfragen");
        securityQuestionsComboBox.setItems(selectedSecurityQuestions);
        securityQuestionsComboBox.setPlaceholder("Sicherheitsfragen auswählen");
        securityQuestionsComboBox.setClearButtonVisible(true);
        securityQuestionsComboBox.setRequired(true);
        securityQuestionsComboBox.setRequiredIndicatorVisible(true);
        securityQuestionsComboBox.setWidth("300px");
        securityQuestionsComboBox.addValueChangeListener(this::onSecurityQuestionsSelected);
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Status");
        radioGroup.setItems("Firma", "Student");
        // radioGroup.setValue("Pending");
        Checkbox checkbox = new Checkbox("Ich stimme den AGBs zu");
        VerticalLayout layout = new VerticalLayout(
                new H2("Registrieren"),
                email,
                vorname,
                nachname,
                passwort,
                passwort2,
                securityQuestionsComboBox,
                answerLayout,
                checkbox,
                radioGroup,
                new Button("Bestätigen", event -> register(
                        email,
                        vorname.getValue(),
                        nachname.getValue(),
                        passwort.getValue(),
                        passwort2.getValue(),
                        checkbox.getValue().toString(),
                        sicherheitsQA,
                        radioGroup.getValue()))

        );
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        answerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private VerticalLayout answerLayout = new VerticalLayout();

    private void onSecurityQuestionsSelected(
            AbstractField.ComponentValueChangeEvent<MultiSelectComboBox<String>, Set<String>> value) {
        Set<String> selectedQuestions = value.getValue();
        sicherheitsQA.clear();
        if (selectedQuestions.size() == 2) {
            answerLayout.removeAll();
            for (String question : selectedQuestions) {

                TextField answerField = new TextField();
                answerField.setLabel("Antwort auf \"" + question + "\":");
                answerField.setWidth("30%");
                answerField.addValueChangeListener(e -> sicherheitsQA.put(question, e.getValue()));
                answerLayout.add(answerField);
            }
        } else if (selectedQuestions.size() < 2) {
            answerLayout.removeAll();
        }
    }

    private void register(EmailField email, String vorname, String nachname, String passwort, String passwort2,
            String checkBox, Map<String, String> sicherheitsQA, String radioValue) {
        if (email.getValue().trim().isEmpty() || !email.getValue().matches(email.getPattern())) {
            Notification.show("Bitte eine gültige Email eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (vorname.trim().isEmpty()) {
            Notification.show("Bitte einen Vornamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (nachname.trim().isEmpty()) {
            Notification.show("Bitte einen Nachnamen eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (!passwort.equals(passwort2)) {
            Notification.show("Passwörter stimmen nicht überein").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (checkBox.equals("false")) {
            Notification.show("Bitte stimme unseren AGB zu").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (sicherheitsQA.size() < 2) {
            Notification.show("Bitte beantworten Sie alle Sicherheitsfragen")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (radioValue == null) {
            Notification.show("Bitte wählen Sie Ihren Status aus").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            String result = userController.register(new User(vorname, nachname, passwort, email.getValue(), sicherheitsQA));
            if (result.equals("email_error")) {
                Notification.show("Email bereits registriert").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            userController.login(email.getValue(), passwort); // get sid cookie (after user registration, before student/company creation)
            Notification.show("Registrierung erfolgreich").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            if (radioValue.equals("Firma")) {
                navigateToCompany();
            } else if (radioValue.equals("Student")) {
                navigateToStudent();
            }
        }
    }
    private void navigateToCompany() {
        UI.getCurrent().navigate(CreateCompanyProfileView.class);
    }

    private void navigateToStudent() {
        UI.getCurrent().navigate(CreateStudentProfileView.class);
    }
}
