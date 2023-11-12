package group.artifact.views;

import group.artifact.entities.Company;
import jakarta.annotation.security.RolesAllowed;
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
        TextField employees = createTextField("Mitarbeiteranzahl");
        DatePicker founded = createDatePicker("Gründungsdatum");
        TextField link = createRequiredTextField("Link zur Unternehmenswebseite");
        TextField description = createRequiredTextField("Beschreibung");
        TextField logo = createTextField("Logo");

        Button createProfileButton = new Button("Bestätigen", event -> createCompanyProfile(
                Integer.parseInt(user.getValue()),
                name.getValue(),
                business.getValue(),
                Integer.parseInt(employees.getValue()),
                founded.getValue(),
                link.getValue(),
                description.getValue(),
                logo.getValue()));

        VerticalLayout layout = new VerticalLayout(
                new H2("Firmenprofil anlegen"),
                user,
                name,
                business,
                employees,
                founded,
                link,
                description,
                logo,
                createProfileButton);
        if (business.isInvalid() | link.isInvalid() | description.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void createCompanyProfile(Integer user, String name, String business, Integer employees, LocalDate founded, String link,
            String description, String logo) {

        Company company = new Company(name, business, employees, founded, link, description, logo);
        try {
            companyController.createCompany(company, user.intValue());
            showSuccessNotification("Firmenprofil erfolgreich angelegt.");
        } catch (DataIntegrityViolationException DIVE) {
            showErrorNotification("Firmenprofil existiert bereits.");
        }
    }

    private TextField createTextField(String label) {
        TextField textField = new TextField(label);
        textField.setWidth("20%");
        return textField;
    }

    private DatePicker createDatePicker(String label) {
        DatePicker datepicker = new DatePicker(String.valueOf(label));
        datepicker.setWidth("20%");
        return datepicker;
    }

    private TextField createRequiredTextField(String label) {
        TextField requiredTextField = new TextField(label);
        requiredTextField.setRequired(true); // Make required field
        requiredTextField.setErrorMessage("Bitte füllen Sie das erforderliche Feld aus.");
        requiredTextField.setWidth("20%");
        return requiredTextField;
    }

    private void showSuccessNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

}
