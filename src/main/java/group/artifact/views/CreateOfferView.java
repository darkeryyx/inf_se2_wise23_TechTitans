package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import group.artifact.controller.OfferController;
import group.artifact.entities.Company;
import group.artifact.entities.Offer;
import group.artifact.repositories.CompanyRepository;
import javax.annotation.security.RolesAllowed;
import org.springframework.dao.DataIntegrityViolationException;

@Route("create/offer")
@RolesAllowed("ROLE_USER")
public class CreateOfferView extends Composite<Component> {
    private OfferController offerController;

    private CompanyRepository companyRepository;

    protected Component initContent() {
        TextField description = createTextField("Beschreibung");
        TextField business = createTextField("Branche");
        TextField income = createTextField("Gehalt");
        TextField job = createTextField("Jobbezeichnung");
        TextField company = createTextField("Firmen_ID"); // TODO: Firma aus Session herauslesen
        TextField offerID = createTextField("Offer_ID");

        Button createOfferButton = new Button("Veröffentlichen", event -> createOffer(
                job.getValue(),
                business.getValue(),
                description.getValue(),
                income.getValue(),
                Integer.parseInt(offerID.getValue()),
                Integer.parseInt(company.getValue())));

        VerticalLayout layout = new VerticalLayout(
                new H2("Firmenprofil anlegen"),
                job,
                business,
                description,
                income,
                company,
                offerID,
                createOfferButton);
        if (business.isInvalid() | job.isInvalid() | description.isInvalid() | income.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void createOffer(String job, String business, String description, String income, Integer company, Integer offerID) {

        Company c = companyRepository.getReferenceById(company);
        Offer offer = new Offer(offerID, c, null,job, business, description, income);
        try {
            offerController.createOffer(offer);
            showSuccessNotification("Firmenprofil erfolgreich angelegt.");
        } catch (DataIntegrityViolationException DIVE) {
            showErrorNotification("Firmenprofil existiert bereits.");
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
