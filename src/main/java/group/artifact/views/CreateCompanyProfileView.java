package group.artifact.views;

import group.artifact.entities.Company;
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
import com.vaadin.flow.component.html.Image;


import java.time.LocalDate;

@Route("company-profile")
public class CreateCompanyProfileView extends Composite<Component>{
    private final CompanyController companyController;

    @Autowired
    public CreateCompanyProfileView(CompanyController companyController) {
        this.companyController = companyController;
    }

    protected Component initContent() {
        TextField business = createRequiredTextField("Firmenname");
        TextField employees = createRequiredTextField("Mitarbeiteranzahl");
        DatePicker founded = createDatePicker("Gründungsdatum");
        TextField link = createTextField("Link zur Unternehmenswebseite");
        TextField description = createTextField("Beschreibung");
        TextField logo = createTextField("Logo");

        Button createProfileButton = new Button("Bestätigen", event -> createCompanyProfile(


                business.getValue(),
                Integer.parseInt(employees.getValue()),
                founded.getValue(),
                link.getValue(),
                description.getValue(),
                logo.getValue()
        ));


        VerticalLayout layout = new VerticalLayout(
                new H2("Firmenprofil anlegen"),
                business,
                employees,
                founded,
                link,
                description,
                logo,
                createProfileButton
        );
        if(business.isInvalid() | employees.isInvalid())
         Notification.show("Bitte füllen Sie das erforderliche Feld aus.") ;
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void createCompanyProfile(String business, Integer employees, LocalDate founded, String link, String description, String logo) {

        Company company = new Company(business, employees, founded,  link, description,logo);
        try {
            companyController.createCompany(company);
            showSuccessNotification("Firmenprofil erfolgreich angelegt.");
        } catch (DataIntegrityViolationException dive) {
            showErrorNotification("Firmenprofil existiert bereits.");
        }
    }

    private TextField createTextField(String label) {
        TextField textField = new TextField(label);
        textField.setWidth("20%");
        return textField;
    }

    private DatePicker createDatePicker(String label){
        DatePicker datepicker = new DatePicker(String.valueOf(label));
        datepicker.setWidth("20%");
        return datepicker;
    }

    private TextField createRequiredTextField(String label){
        TextField requiredTextField = new TextField(label);
        requiredTextField.setRequired(true); //Make required field
        requiredTextField.setErrorMessage("Bitte füllen Sie das erforderliche Feld aus.");
        return requiredTextField;
    }


    private void showSuccessNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }


}
