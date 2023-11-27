package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import group.artifact.controller.CompanyController;
import group.artifact.controller.OfferController;
import group.artifact.entities.Company;
import group.artifact.entities.Offer;

import javax.annotation.security.RolesAllowed;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Route("create/offer")
@RolesAllowed("ROLE_USER")
public class CreateOfferView extends Composite<Component> {
    private OfferController offerController;

    private CompanyController companyController;

    public CreateOfferView(OfferController offerController, CompanyController companyController) {
        this.offerController = offerController;
        this.companyController = companyController;
    }

    protected Component initContent() {
        TextField description = createTextField("Beschreibung");
        ComboBox<String> business = new ComboBox<>("Branche");
        NumberField income = new NumberField("Stundenlohn");
        TextField job = createTextField("Jobbezeichnung");
        TextField company = createTextField("Firmen_ID"); // TODO: Firma aus Session herauslesen
        List<String> businessList = offerController.getBusinessList();
        ComboBox.ItemFilter<String> filter = (b, filterString) -> b
                .toLowerCase().startsWith(filterString.toLowerCase());
        business.setItems(filter, businessList);

        Button createOfferButton = new Button("Veröffentlichen", event -> createOffer(
                job.getValue(),
                business.getValue(),
                description.getValue(),
                income.getValue().floatValue(),
                Integer.parseInt(company.getValue())));

        VerticalLayout layout = new VerticalLayout(
                new H2("Jobausschreibung erstellen"),
                job,
                business,
                description,
                income,
                company,
                createOfferButton);
        if (business.isInvalid() | job.isInvalid() | description.isInvalid() | income.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void createOffer(String job, String business, String description, Float income, Integer company) {

        Optional<Company> c = companyController.findByID(company);
        if(c.isPresent()) {
            Offer offer = new Offer(c.get(), job, business, description, income);
            try {
                offerController.createOffer(offer);
                showSuccessNotification("Jobausschreibung erfolgreich angelegt.");
            } catch (DataIntegrityViolationException DIVE) {
                showErrorNotification("Jobausschreibung existiert bereits.");
            }
        } else {
            showErrorNotification("Unternehmen existiert nicht");
        }
    }

    private TextField createTextField(String label) {
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
