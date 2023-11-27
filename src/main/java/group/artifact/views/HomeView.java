package group.artifact.views;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("home")
@RolesAllowed("ROLE_USER")
public class HomeView extends VerticalLayout {

    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    HomeView() {
        Anchor login = new Anchor("login", "Login");
        Anchor register = new Anchor("register", "Registrieren");
        Anchor registerVerify = new Anchor("register/verify", "E-Mail bestätigen");
        Anchor forgotPW = new Anchor("forgotPW", "Passwort vergessen?");

        Anchor createProfile = new Anchor("create/profile", "Profil erstellen");
        Anchor createOfferView = new Anchor("create/offers", "Angebot erstellen");

        Anchor searchCompaniesView = new Anchor("search/companies", "Unternehmen suchen");
        Anchor searchOffersView = new Anchor("search/offers", "Angebote suchen");

        Anchor viewStudentProfile = new Anchor("view/student", "Studentenprofil ansehen");

        header = new HorizontalLayout(login, register, registerVerify, forgotPW, createProfile, createOfferView, searchCompaniesView, searchOffersView, viewStudentProfile);

        content = new VerticalLayout();

        fooder = new HorizontalLayout();

        content.setWidth("80%");

    }

    HomeView(VerticalLayout content) {
        header = new HorizontalLayout(

        );

        this.content = content;

        fooder = new HorizontalLayout(

        );
    }

    void setHeader(HorizontalLayout header) {
        this.header = header;
    }

    void setContent(VerticalLayout content) {
        this.content = content;
    }

    void setFooder(HorizontalLayout fooder) {
        this.fooder = fooder;
    }

    void add(){
        add(header, content, fooder);
    }

}
