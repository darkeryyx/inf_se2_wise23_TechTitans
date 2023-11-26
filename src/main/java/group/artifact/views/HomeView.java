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
        header = new HorizontalLayout(

        );

        content = new VerticalLayout();

        fooder = new HorizontalLayout(

        );
        add(header, content, fooder);

        Anchor login = new Anchor("login", "Login");
        Anchor register = new Anchor("register", "Registrieren");
        Anchor registerVerify = new Anchor("register/verify", "E-Mail bestätigen");
        Anchor forgotPW = new Anchor("forgotPW", "Passwort vergessen?");

        Anchor createProfile = new Anchor("create/profile", "Profil erstellen");
        Anchor createOfferView = new Anchor("create/offers", "Angebot erstellen");

        Anchor searchCompaniesView = new Anchor("search/companies", "Unternehmen suchen");
        Anchor searchOffersView = new Anchor("search/offers", "Angebote suchen");

        Anchor viewStudentProfile = new Anchor("view/student", "Studentenprofil ansehen");
        add(login, register, registerVerify, forgotPW, createProfile, createOfferView, searchCompaniesView, searchOffersView, viewStudentProfile);
    }

    HomeView(VerticalLayout content) {
        header = new HorizontalLayout(

        );

        this.content = content;

        fooder = new HorizontalLayout(

        );
        add(header, this.content, fooder);
    }

    void addHeader(HorizontalLayout header) {
        this.header = header;
        add(this.header, content, fooder);
    }

    void addContent(VerticalLayout content) {
        this.content = content;
        add(header, this.content, fooder);
    }

    void addFooder(HorizontalLayout fooder) {
        this.fooder = fooder;
        add(header, content, fooder);
    }

}
