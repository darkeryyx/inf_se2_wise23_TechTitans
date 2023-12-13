package group.artifact.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;

@CssImport("./generated/jar-resources/css/StudentView.css")
public class StudentView extends VerticalLayout {

    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    StudentView() {
        this.getStyle().set("padding","0");
        this.setAlignItems(Alignment.CENTER);
        MenuBar menuBar = new MenuBar();
        MenuItem placeholder = menuBar.addItem("");
        placeholder.setEnabled(false);
        MenuItem searchCompanies = menuBar.addItem(createMenuContent("Unternehmen", VaadinIcon.SEARCH),
                e -> {UI.getCurrent().navigate(SearchCompaniesView.class);});
        MenuItem searchOffer = menuBar.addItem(createMenuContent("Stellenangebote", VaadinIcon.SEARCH),
                e -> {UI.getCurrent().navigate(SearchOffersView.class);});
        MenuItem messages = menuBar.addItem(new Icon(VaadinIcon.ENVELOPE),
                e -> {UI.getCurrent().navigate(MyOffersView.class);});
        MenuItem profilDropdown = menuBar.addItem(new Avatar());
        profilDropdown.getSubMenu().addItem("Mein Profil",
                e -> {UI.getCurrent().navigate(ViewStudentProfile.class);});
        profilDropdown.getSubMenu().addItem("Meine Stellenangebote",
                e -> {UI.getCurrent().navigate(MyOffersView.class);});
        profilDropdown.getSubMenu().addItem("Abmelden");

        header = new HorizontalLayout(menuBar);
        header.setWidth("100vw");
        header.addClassName("header-fooder");
        header.setAlignItems(Alignment.CENTER);

        placeholder.getElement().getStyle().set("width", "54vw");
        searchCompanies.getElement().getStyle().set("width", "15vw");
        searchOffer.getElement().getStyle().set("width", "15vw");
        messages.getElement().getStyle().set("width", "6vw");
        profilDropdown.getElement().getStyle().set("width", "7vw");


        content = new VerticalLayout(

        );

        fooder = new HorizontalLayout(

        );
        fooder.addClassName("header-fooder");

    }

    StudentView(VerticalLayout content) {

        header = new HorizontalLayout();

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

    void add() {
        add(this.header, this.content, this.fooder);
    }

    private Span createMenuContent(String text, VaadinIcon icon) {
        Span menuContent = new Span();
        menuContent.add(new Icon(icon), new Label(text));
        return menuContent;
    }
}
