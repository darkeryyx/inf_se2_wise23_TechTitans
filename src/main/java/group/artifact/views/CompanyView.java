package group.artifact.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class CompanyView extends BasisView{

    CompanyView() {
        super();
        menuBar.setEnabled(true);

        MenuItem searchStudent = menuBar.addItem(createMenuContent("Studenten", VaadinIcon.SEARCH));
        //        ,e -> {UI.getCurrent().navigate(SearchStudentView.class);});
        MenuItem messages = menuBar.addItem(new Icon(VaadinIcon.ENVELOPE),
                e -> {UI.getCurrent().navigate(MyOffersView.class);});

        MenuItem profilDropdown = menuBar.addItem(new Avatar());
        profilDropdown.getSubMenu().addItem("Mein Profil",
                e -> {UI.getCurrent().navigate(ViewCompanyProfile.class);});
        profilDropdown.getSubMenu().addItem("Meine Stellenangebote",
                e -> {UI.getCurrent().navigate(MyOffersView.class);});
        profilDropdown.getSubMenu().addItem("Abmelden");


        this.header = new HorizontalLayout(menuBar);

        menuBar.getItems().get(0).getElement().getStyle().set("width","69vw");
        searchStudent.getElement().getStyle().set("width", "15vw");
        messages.getElement().getStyle().set("width", "6vw");
        profilDropdown.getElement().getStyle().set("width", "7vw");

    }

}
