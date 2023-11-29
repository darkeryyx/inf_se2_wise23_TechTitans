package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import group.artifact.controller.CompanyController;
import group.artifact.controller.OfferController;
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;

import group.artifact.dtos.OfferDTO;
import group.artifact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("login")
@AnonymousAllowed
@CssImport("./css/LoginView.css")
public class LoginView extends Composite<Component> {

    @Autowired
    private UserController userController;
    @Autowired
    private OfferController offerController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private CompanyController companyController;

    protected Component initContent() {
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Passwort");
        Anchor pwVergessen = new Anchor("forgotPW", "Passwort vergessen?");
        Anchor account = new Anchor("register", "Account erstellen");

        password.addKeyDownListener(Key.ENTER, event -> {
            login(email.getValue(), password.getValue());
        });

        email.addKeyDownListener(Key.ENTER, event -> {
            login(email.getValue(), password.getValue());
        });

        Button submit = new Button("Bestätigen", event -> login(
                email.getValue(),
                password.getValue()));
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        H2 loginHeader = new H2("Login");

        email.focus();

        VerticalLayout loginFormLayout = new VerticalLayout(
                loginHeader,
                email,
                password,
                submit);

        loginFormLayout.add(pwVergessen);
        loginFormLayout.add(account);

        //loginFormLayout.setHeight("100%");

        /*
        loginFormLayout.getStyle().set("background-image", "url(images/image.png)");
        loginFormLayout.getStyle().set("background-size", "cover");

         */

        loginFormLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();
        for(int i = 0; i < 10; i++) {
            offers.add(offers.get(0));
        }


        VerticalLayout jobAdvertisementsLayout = new VerticalLayout();
        jobAdvertisementsLayout.getStyle().set("max-height", "90%");
        jobAdvertisementsLayout.getStyle().set("overflow-y", "auto");

        for(OfferDTO offerDTO : offers) {
            jobAdvertisementsLayout.add(createJobAdvertisementComponent(offerDTO));
        }
        jobAdvertisementsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout mainLayout = new HorizontalLayout(jobAdvertisementsLayout, loginFormLayout);
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return mainLayout;
    }

    private Component createJobAdvertisementComponent(OfferDTO offerDTO) {
        Div test = new Div();
        H3 job = new H3(offerDTO.getJob());
        H5 company = new H5(offerDTO.getCompanyName());
        Span description = new Span(offerDTO.getDescription());
        test.add(job);
        test.add(company);
        test.add(description);

        test.setWidth("95%");

        test.getStyle().set("border", "1px solid #aaaaaa");
        test.getStyle().set("padding", "10px");

        test.getStyle().set("transition", "background-color 0.3s ease");
        test.getStyle().set("cursor", "pointer");

        test.getElement().addEventListener("mouseover", event -> {
            getUI().ifPresent(ui -> ui.access(() ->
                    test.getStyle().set("background-color", "#f0f0f0")
            ));
        });

        test.getElement().addEventListener("mouseout", event -> {
            getUI().ifPresent(ui -> ui.access(() ->
                    test.getStyle().remove("background-color")
            ));
        });
        return test;
    }

    private void login(String email, String password) {
        if (email.trim().isEmpty()) {
            Notification.show("Bitte E-Mail eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (password.trim().isEmpty()) {
            Notification.show("Bitte Passwort eingeben").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            if (userController.login(email, password)) {
                System.out.println("Login erfolgreich");
                getUI().ifPresent(ui -> ui.access(() -> {
                    ui.navigate("/home");
                    User user = userController.getCurrentUser();
                    //System.out.println(user==null);
                    if (studentController.studentExists(user.getUser_pk()) || (companyController.companyExists(user.getUser_pk()))) {
                        System.out.println("User hat Profil");
                        Notification.show("Login erfolgreich!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        System.out.println("User hat kein Profil");
                        Notification notification = new Notification();
                        Anchor ref = new Anchor("create/profile", "Profil erstellen");
                        Div text = new Div(new Text("Sie haben noch kein Profil erstellt. Um ein Profil zu erstellen, klicken Sie hier:"));
                        Button closeButton = new Button(new Icon("lumo", "cross"));
                        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                        closeButton.getElement().setAttribute("aria-label", "Close");
                        closeButton.addClickListener(event -> {
                            notification.close();
                        });
                        HorizontalLayout layout = new HorizontalLayout(text, ref, closeButton);
                        layout.setAlignItems(FlexComponent.Alignment.CENTER);
                        notification.add(layout);

                        Notification.show("Login erfolgreich!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.open();
                    }
                }));
            } else {
                Notification.show("Falsche Email oder Passwort!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

}