package group.artifact.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.awt.*;

@CssImport("./generated/jar-resources/css/StudentView.css")
public class StudentView extends BasisView {

    StudentView() {
        super();
        menuBar.setEnabled(true);

        //placeholder.setEnabled(false);

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


        this.header = new HorizontalLayout(menuBar);

        menuBar.getItems().get(0).getElement().getStyle().set("width","54vw");
        searchCompanies.getElement().getStyle().set("width", "15vw");
        searchOffer.getElement().getStyle().set("width", "15vw");
        messages.getElement().getStyle().set("width", "6vw");
        profilDropdown.getElement().getStyle().set("width", "7vw");

    }

}
