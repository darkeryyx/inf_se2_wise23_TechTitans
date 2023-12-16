package group.artifact.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;

import java.awt.*;

public class BasisView extends VerticalLayout {

    MenuBar menuBar;
    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    BasisView(){
        menuBar = new MenuBar();
        menuBar.setEnabled(false);
        menuBar.setWidth("100vw");

        header = new HorizontalLayout(menuBar);
        content = new VerticalLayout();

        fooder = new HorizontalLayout();
    }

    public void addViews (){

        MenuItem views = this.menuBar.addItem("Views");
        views.getElement().getStyle().set("width", "54vw");
        MenuItem createCompanyProfile= views.getSubMenu().addItem("createCompanyProfile",
                e -> {
                    UI.getCurrent().navigate(CreateCompanyProfileView.class);});

        MenuItem createOffer = views.getSubMenu().addItem("createOffer",
                e -> {UI.getCurrent().navigate(CreateCompanyProfileView.class);});

        MenuItem createProfile = views.getSubMenu().addItem("createProfile",
                e -> {UI.getCurrent().navigate(CreateProfile.class);});

        MenuItem createStudentProfile = views.getSubMenu().addItem("createStudentProfile",
                e -> {UI.getCurrent().navigate(CreateStudentProfileView.class);});

        MenuItem datenschutz = views.getSubMenu().addItem("Datenschutzerklärung",
                e -> {UI.getCurrent().navigate(DatenschutzerklaerungView.class);});

        MenuItem forgotPWVerification = views.getSubMenu().addItem("forgotPWVerification",
                e -> {UI.getCurrent().navigate(ForgotPWVerificationView.class);});

        MenuItem forgotPW = views.getSubMenu().addItem("forgotPWView",
                e -> {UI.getCurrent().navigate(forgotPWView.class);});

        MenuItem home = views.getSubMenu().addItem("Home",
                e -> {UI.getCurrent().navigate(HomeView.class);});

        MenuItem login = views.getSubMenu().addItem("Login",
                e -> {UI.getCurrent().navigate(LoginView.class);});

        MenuItem myOffers = views.getSubMenu().addItem("myOffers",
                e -> {UI.getCurrent().navigate(MyOffersView.class);});

        MenuItem registerVerification = views.getSubMenu().addItem("RegisterVerification",
                e -> {UI.getCurrent().navigate(RegisterVerificationView.class);});

        MenuItem register = views.getSubMenu().addItem("Register",
                e -> {UI.getCurrent().navigate(RegisterView.class);});

        MenuItem searchComp = views.getSubMenu().addItem("searchCompanies",
                e -> {UI.getCurrent().navigate(SearchCompaniesView.class);});

        MenuItem searchOffersView = views.getSubMenu().addItem("searchOffer",
                e -> {UI.getCurrent().navigate(SearchOffersView.class);});

        MenuItem companyProfile = views.getSubMenu().addItem("CompanyProfile",
                e -> {UI.getCurrent().navigate(ViewCompanyProfile.class);});

        MenuItem studentProfile = views.getSubMenu().addItem("StudentProfile",
                e -> {UI.getCurrent().navigate(ViewStudentProfile.class);});

        menuBar.setEnabled(true);
    }

    public MenuBar createBasicMenuBar(){
        MenuBar menuBar = new MenuBar();
        MenuItem views = menuBar.addItem("Views");
        views.getElement().getStyle().set("width", "54vw");
        MenuItem createCompanyProfile= views.getSubMenu().addItem("createCompanyProfile",
                e -> {
                    UI.getCurrent().navigate(CreateCompanyProfileView.class);});

        MenuItem createOffer = views.getSubMenu().addItem("createOffer",
                e -> {UI.getCurrent().navigate(CreateCompanyProfileView.class);});

        MenuItem createProfile = views.getSubMenu().addItem("createProfile",
                e -> {UI.getCurrent().navigate(CreateProfile.class);});

        MenuItem createStudentProfile = views.getSubMenu().addItem("createStudentProfile",
                e -> {UI.getCurrent().navigate(CreateStudentProfileView.class);});

        MenuItem datenschutz = views.getSubMenu().addItem("Datenschutzerklärung",
                e -> {UI.getCurrent().navigate(DatenschutzerklaerungView.class);});

        MenuItem forgotPWVerification = views.getSubMenu().addItem("forgotPWVerification",
                e -> {UI.getCurrent().navigate(ForgotPWVerificationView.class);});

        MenuItem forgotPW = views.getSubMenu().addItem("forgotPWView",
                e -> {UI.getCurrent().navigate(forgotPWView.class);});

        MenuItem home = views.getSubMenu().addItem("Home",
                e -> {UI.getCurrent().navigate(HomeView.class);});

        MenuItem login = views.getSubMenu().addItem("Login",
                e -> {UI.getCurrent().navigate(LoginView.class);});

        MenuItem myOffers = views.getSubMenu().addItem("myOffers",
                e -> {UI.getCurrent().navigate(MyOffersView.class);});

        MenuItem registerVerification = views.getSubMenu().addItem("RegisterVerification",
                e -> {UI.getCurrent().navigate(RegisterVerificationView.class);});

        MenuItem register = views.getSubMenu().addItem("Register",
                e -> {UI.getCurrent().navigate(RegisterView.class);});

        MenuItem searchComp = views.getSubMenu().addItem("searchCompanies",
                e -> {UI.getCurrent().navigate(SearchCompaniesView.class);});

        MenuItem searchOffersView = views.getSubMenu().addItem("searchOffer",
                e -> {UI.getCurrent().navigate(SearchOffersView.class);});

        MenuItem companyProfile = views.getSubMenu().addItem("CompanyProfile",
                e -> {UI.getCurrent().navigate(ViewCompanyProfile.class);});

        MenuItem studentProfile = views.getSubMenu().addItem("StudentProfile",
                e -> {UI.getCurrent().navigate(ViewStudentProfile.class);});

        menuBar.setEnabled(true);
        return menuBar;
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

    void add() {
        add(this.header, this.content, this.fooder);
    }

    static Span createMenuContent(String text, VaadinIcon icon) {
        Span menuContent = new Span();
        menuContent.add(new Icon(icon), new Label(text));
        return menuContent;
    }
}
