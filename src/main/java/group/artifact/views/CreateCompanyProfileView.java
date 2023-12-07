package group.artifact.views;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
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

        Div container = new Div(); // Container to hold layout
        container.setWidth("100%"); // Full width of the page

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
        createProfileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        ProgressBar progressBar = new ProgressBar();

        createProfileButton.addClickListener(e -> progressBar.setValue(0.5));
        createProfileButton.addClickListener(e -> progressBar.setValue(1.0));
        //create cancel button. button goes back to Log in view

        Button cancelButton = new Button("Abbrechen", e -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));  //when user is present navigate back to log in view
            Notification.show("Firmenprofilerstellung abgebrochen!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        });


        // Add spacing between buttons
        buttonLayout.setSpacing(true);

        //SetWidthfull
        buttonLayout.setWidthFull();

        // Adjust alignment to center
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Increase button size for better visibility
        createProfileButton.setWidth("200px");
        cancelButton.setWidth("200px");


        // Add buttons to the layout
        buttonLayout.add(createProfileButton, cancelButton);

/**
 * //Vertical layout v1
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
 **/

        //create vertical layout which is the main layout of the view
        VerticalLayout layout = new VerticalLayout(
                new H2("Firmenprofil anlegen"),
                createFormLayout(user, name, business, employees, founded, link, logo, description),
                buttonLayout
        );
        if (business.isInvalid() | link.isInvalid() | description.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        // Make the layout more compact
        layout.setPadding(true);

        // Center the layout
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, buttonLayout);

        // Set a fixed width or maximum width to control the layout size
        layout.setMaxWidth("80em");
        //layout.setMargin(true);

        // Adjust the width of the form components for better responsiveness
        user.setWidthFull();
        name.setWidthFull();
        business.setWidthFull();
        employees.setWidthFull();
        founded.setWidthFull();
        link.setWidthFull();
        logo.setWidthFull();

        //add layout to container
        container.add(layout);

        // Center the container on the page
        container.getStyle().set("margin", "auto");
        container.getStyle().set("display", "flex");
        container.getStyle().set("flexDirection", "column");
        container.getStyle().set("alignItems", "center");

        return container;
    }


    private void createCompanyProfile(Integer user, String name, String business, Integer employees, LocalDate founded, String link,
            String description, String logo) {

        Company company = new Company(name, business, employees, founded, link, description, logo);
        try {
            // companyController.createCompany(company, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate(RegisterVerificationView.class);
            }));
            showSuccessNotification(name);
        } catch (DataIntegrityViolationException DIVE) {
            showErrorNotification();
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
        textArea.setHelperText("Beschreiben Sie Ihr Unternehmen kurz");
        textArea.setPlaceholder("Schreiben Sie hier . . . ");
        textArea.setWidth("80%");
        textArea.setHeight("30%");
        textArea.setClearButtonVisible(true);

        return textArea;

    }

    public IntegerField createIntegerField(String label){
      IntegerField integerField = new IntegerField(label);
        integerField.setHelperText("mind. 1 Mitarbeiter(in)");
        integerField.setMin(1);
        integerField.setValue(1);
        integerField.setWidth("10%");
        integerField.setStepButtonsVisible(true);
        return integerField;
    }

    private FormLayout createFormLayout(TextField user, TextField name, TextField business, IntegerField employees, DatePicker founded, TextField link, TextField logo, TextArea description) {
        FormLayout formLayout = new FormLayout();
        formLayout.add(user, name, business, employees, founded, link, logo, description);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("21em", 2),
                new FormLayout.ResponsiveStep("32em", 3)
        );
        return formLayout;
    }

    private void showSuccessNotification(String component) {
        Notification.show("Firmenprofil für " + component + "erfolgreich angelegt").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification() {
        Notification.show("Firmenprofil existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

}
