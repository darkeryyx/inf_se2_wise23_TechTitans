package group.artifact.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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
        FlexLayout mainLayout = new FlexLayout();
        mainLayout.setSizeFull();

        // Job Advertisement Component
        Component jobAdvertisementComponent = createJobAdvertisementListComponent();
        jobAdvertisementComponent.getElement().getStyle().set("max-height", "95%");
        jobAdvertisementComponent.getElement().getStyle().set("overflow-y", "auto");
        mainLayout.add(jobAdvertisementComponent);

        // Login Component
        Component loginComponent = createLoginComponent();
        mainLayout.add(loginComponent);

        mainLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return mainLayout;
    }

    private Component createJobAdvertisementListComponent() {
        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();

        // Cheat to have more than 5 job advertisements
        for(int i = 0; i < 10; i++) {
            offers.add(offers.get(0));
        }

        FlexLayout jobAdvertisementsLayout = new FlexLayout();
        jobAdvertisementsLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        jobAdvertisementsLayout.setWidth("50%");
        jobAdvertisementsLayout.getStyle().set("overflow-y", "auto");

        // If we have 20+ Offers not every offer can be used
        // Maybe a fancy ML solution
        for(OfferDTO offerDTO : offers) {
            jobAdvertisementsLayout.add(createJobAdvertisementComponent(offerDTO));
        }
        jobAdvertisementsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return jobAdvertisementsLayout;
    }

    private Component createJobAdvertisementComponent(OfferDTO offerDTO) {
        Div jobAdvertisementComponent = new Div();
        H3 job = new H3(offerDTO.getJob());
        H5 company = new H5(offerDTO.getCompanyName());
        Span description = new Span(offerDTO.getDescription());
        jobAdvertisementComponent.add(job);
        jobAdvertisementComponent.add(company);
        jobAdvertisementComponent.add(description);

        jobAdvertisementComponent.setWidth("95%");

        jobAdvertisementComponent.getStyle().set("border", "1px solid #aaaaaa");
        jobAdvertisementComponent.getStyle().set("padding", "10px");

        jobAdvertisementComponent.getStyle().set("transition", "background-color 0.3s ease");
        jobAdvertisementComponent.getStyle().set("cursor", "pointer");

        jobAdvertisementComponent.getElement().addEventListener("mouseover", event -> {
            getUI().ifPresent(ui -> ui.access(() ->
                    jobAdvertisementComponent.getStyle().set("background-color", "#f0f0f0")
            ));
        });

        jobAdvertisementComponent.getElement().addEventListener("mouseout", event -> {
            getUI().ifPresent(ui -> ui.access(() ->
                    jobAdvertisementComponent.getStyle().remove("background-color")
            ));
        });
        return jobAdvertisementComponent;
    }

    private Component createLoginComponent() {
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Passwort");
        Anchor forgotPW = new Anchor("forgotPW", "Passwort vergessen?");
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

        FlexLayout loginFormLayout = new FlexLayout(
                loginHeader,
                email,
                password,
                submit);
        loginFormLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        loginFormLayout.setWidth("50%");

        loginFormLayout.add(forgotPW);
        loginFormLayout.add(account);

        loginFormLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //loginFormLayout.setHeight("100%");

        /*
        loginFormLayout.getStyle().set("background-image", "url(images/image.png)");
        loginFormLayout.getStyle().set("background-size", "cover");

         */

        return loginFormLayout;
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
                        ref.getStyle().set("text-decoration", "underline");
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
                Notification.show("Falsche E-Mail oder falsches Passwort!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

}