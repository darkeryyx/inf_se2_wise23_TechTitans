package group.artifact.views;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
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

@Route("search-Offers")
public class SearchOffersView extends Composite<Component> {


    protected Component initContent() {
        final Grid<Offer> grid = new Grid<>(Offer.class, false);
        grid.setItems(); //Funktion: getOfferStream()
        grid.setWidth("950px");

        grid.addColumn(item -> {
            Image image = new Image(); // Funktion: item.getCompany().getImage()
            image.setWidth("50px");
            return image;
        }).setHeader("Logo").setWidth("50px");
        grid.addColumn(Offer::getCompany).setHeader("Unternehmen").setWidth("150px");
        grid.addColumn(Offer::getJob).setHeader("Stellenangebote").setWidth("550px");
        grid.addColumn(Offer::getIncome).setHeader("€/h").setWidth("50px");

        TextField searchText = new TextField("Suchfeld", "Geben Sie hier einen Jobtitel oder ein Unternehmen ein..");
        searchText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        NumberField minSalary = new NumberField("min ..€/h", "z.B. min 15€/h");


        //Breiten und Höhen
        minSalary.setWidth("150px");
        searchText.setWidth("770px");

        RouterLink searchCompanyViewLink = new RouterLink(SearchCompaniesView.class);
        searchCompanyViewLink.setText("Nach Unternehmen suchen");

        VerticalLayout layoutOffer = new VerticalLayout(
                new HorizontalLayout(
                        searchCompanyViewLink
                ),
                new HorizontalLayout(
                        searchText,
                        minSalary
                ),
                grid
        );

        return layoutOffer;

    }



}
