package group.artifact.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import group.artifact.entities.Company;
import javax.annotation.security.RolesAllowed;
import group.artifact.controller.CompanyController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.LocalDate;

@Route("create/company")
@RolesAllowed("ROLE_USER")
public class CreateCompanyProfileView extends Composite<Component> {
    private final CompanyController companyController;

    @Autowired
    public CreateCompanyProfileView(CompanyController companyController) {
        this.companyController = companyController;
    }

    protected Component initContent() {
        TextField user = createRequiredTextField("User-ID");
        TextField name = createRequiredTextField("Firmenname");
        TextField business = createRequiredTextField("Branche");
         var employees = createIntegerField("Mitarbeiteranzahl");
        DatePicker founded = createDatePicker("Gründungsdatum");
        TextField link = createRequiredTextField("Webseite");
        TextField logo = createTextField("Logo");
        var description = createTextArea("Beschreibung");
        HorizontalLayout buttonLayout = new HorizontalLayout();


        //create submit button
        Button createProfileButton = new Button("Bestätigen", event -> createCompanyProfile(
                Integer.parseInt(user.getValue()),
                name.getValue(),
                business.getValue(),
                employees.getValue(),
                founded.getValue(),
                link.getValue(),
                logo.getValue(),
                description.getValue()
                ));
        createProfileButton.addClickListener(e -> Notification.show("Unternehmen" + name.getValue() + "erfolgreich angelegt"));

        //create cancel button. button goes back to Log in view
        Button cancelButton = new Button("Abbrechen", e -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));  //when user is present navigate back to log in view
            Notification.show("Firmenprofilerstellung abgebrochen!");
        });

        buttonLayout.add(createProfileButton, cancelButton);
        buttonLayout.setSpacing(true);


        VerticalLayout layout = new VerticalLayout(
                new H2("Firmenprofil anlegen"),
                user,
                name,
                business,
                employees,
                founded,
                link,
                logo,
                description,
                buttonLayout);
        if (business.isInvalid() | link.isInvalid() | description.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSizeFull();
        return layout;
    }

    private void createCompanyProfile(Integer user, String name, String business, Integer employees, LocalDate founded, String link,
            String description, String logo) {

        Company company = new Company(name, business, employees, founded, link, description, logo);
        try {
            companyController.createCompany(company, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate(RegisterVerificationView.class);
            }));
            showSuccessNotification("Firmenprofil erfolgreich angelegt.");
        } catch (DataIntegrityViolationException DIVE) {
            showErrorNotification("Firmenprofil existiert bereits.");
        }
    }

    private TextField createTextField(String label) {
        TextField textField = new TextField(label);
        textField.setWidth("10%");
        return textField;
    }

    private DatePicker createDatePicker(String label) {
        DatePicker datepicker = new DatePicker(String.valueOf(label));
        datepicker.setWidth("10%");
        return datepicker;
    }

    private TextField createRequiredTextField(String label) {
        TextField requiredTextField = new TextField(label);
        requiredTextField.setRequired(true); // Make required field
        requiredTextField.setErrorMessage("Bitte füllen Sie das erforderliche Feld aus.");
        requiredTextField.setWidth("10%");
        return requiredTextField;
    }

    public TextArea createTextArea(String label){
        TextArea textArea = new TextArea(label);
        textArea.setLabel("Label");
        textArea.setHelperText("beschreiben Sie Ihr Unternehmen kurz");
        textArea.setPlaceholder("Schreiben Sie hier . . . ");
        textArea.setWidth("30%");
        textArea.setHeight("30%");
        textArea.setClearButtonVisible(true);

        return textArea;

    }

    public IntegerField createIntegerField(String label){
      IntegerField integerField = new IntegerField(label);
        integerField.setHelperText("mind. 1 Mitarbeiter(in)");
        integerField.setMin(1);
        integerField.setValue(1);
        integerField.setStepButtonsVisible(true);
        return integerField;
    }

    private void showSuccessNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

}
