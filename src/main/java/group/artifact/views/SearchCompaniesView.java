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
import group.artifact.repositories.CompanyRepository;
import jakarta.annotation.security.RolesAllowed;

@Route("search/company")
@RolesAllowed("ROLE_USER")
public class SearchCompaniesView extends MainView {

    CompanyRepository companyRepository;

    public SearchCompaniesView(){
        super(content());
    }

    static VerticalLayout content() {

        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("Nach Stellenangebote suchen");

        //Grid
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

        //Searching Parameters
        MultiSelectComboBox<String> businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(); //Funktion: getBusinesses gruppiert

        TextField searchField = new TextField("Suchfeld", "Geben Sie hier einen Firmennamen ein");
        searchField.setPrefixComponent( new Icon (VaadinIcon.SEARCH));

        //Width and Hight
        searchField.setWidth("720px");
        businessComboBox.setWidth("150px");


        //Content Layout
        return new VerticalLayout(
                new HorizontalLayout(
                searchOffersViewLink
                ),
                new HorizontalLayout(
                        businessComboBox,
                        searchField
                ),
                grid
        );

    }

}
