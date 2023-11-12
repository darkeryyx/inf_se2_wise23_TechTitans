package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;
import group.artifact.entities.Company;
import jakarta.annotation.security.RolesAllowed;

@Route("search/company")
@RolesAllowed("ROLE_USER")
public class SearchCompaniesView extends Composite<Component> {

    protected Component initContent() {

        final Grid<Company> grid = new Grid<>(Company.class, false);
        grid.setItems(); // Funktion: getCompanyStream()
        grid.setWidth("900px");

        grid.addColumn(item -> {
            Image image = new Image(); // Funktion: getImage()
            image.setWidth("100px");
            return image;
        }).setHeader("Logo").setWidth("100px");
        grid.addColumn(Company:: getUser ).setHeader("Unternehmen").setWidth("600px");
        grid.addColumn(Company:: getLink).setHeader("Link").setWidth("160px");


        MultiSelectComboBox<String> BusinessComboBox = new MultiSelectComboBox<>("Branchen");
        BusinessComboBox.setItems(); //Funktion: getBusinesses gruppiert

        TextField searchField = new TextField("Suchfeld", "Geben Sie hier einen Firmennamen ein");
        searchField.setPrefixComponent( new Icon (VaadinIcon.SEARCH));

        //Konfiguration Breiten und Höhen
        searchField.setWidth("720px");
        BusinessComboBox.setWidth("150px");

        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("Nach Stellenangebote suchen");

        VerticalLayout layoutCompany = new VerticalLayout(
                new HorizontalLayout(
                searchOffersViewLink
                ),
                new HorizontalLayout(
                        BusinessComboBox,
                        searchField
                ),
                grid
        );

        return layoutCompany;
    }

}
