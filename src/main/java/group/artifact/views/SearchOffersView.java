package group.artifact.views;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import group.artifact.entities.Offer;
import group.artifact.repositories.OfferRepository;
import jakarta.annotation.security.RolesAllowed;

@Route("search/offer")
@RolesAllowed("ROLE_USER")
public class SearchOffersView extends MainView {

    private OfferRepository offerRepository;

    SearchOffersView(){
        super(content());
    }

    static VerticalLayout content() {

        RouterLink searchCompanyViewLink = new RouterLink(SearchCompaniesView.class);
        searchCompanyViewLink.setText("Nach Unternehmen suchen");

        //Grid
        final Grid<Offer> grid = new Grid<>(Offer.class, false);
        grid.setItems();
        grid.setWidth("1100px");

        grid.addColumn(item -> {
            Image image = new Image(); // Funktion: item.getCompany().getImage()
            image.setWidth("50px");
            return image;
        }).setHeader("Logo").setWidth("50px");
        grid.addColumn(Offer::getCompany).setHeader("Unternehmen").setWidth("150px");
        grid.addColumn(Offer::getJob).setHeader("Stellenangebote").setWidth("700px");
        grid.addColumn(Offer::getIncome).setHeader("€/h").setWidth("50px");

        //Searching Parameters
        MultiSelectComboBox<String> businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(); //Funktion: getBusinesses gruppiert
        TextField searchText = new TextField("Suchfeld", "Geben Sie hier einen Jobtitel oder ein Unternehmen ein..");
        searchText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        NumberField minIncome = new NumberField("min ..€/h", "z.B. min 15€/h");

        //Width and Hight
        minIncome.setWidth("150px");
        searchText.setWidth("770px");
        businessComboBox.setWidth("150px");

        return new VerticalLayout(
                new HorizontalLayout(
                        searchCompanyViewLink
                ),
                new HorizontalLayout(
                        businessComboBox,
                        searchText,
                        minIncome
                ),
                grid
        );

    }



}
