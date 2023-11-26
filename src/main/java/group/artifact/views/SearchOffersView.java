package group.artifact.views;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import group.artifact.controller.OfferController;
import group.artifact.dtos.OfferDTO;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;

import java.util.List;
import java.util.stream.Collectors;

@Route("search/offers")
@RolesAllowed("ROLE_USER")
public class SearchOffersView extends HomeView {

    private final OfferController offerController;
    public SearchOffersView(OfferController offerController) {
        super();
        this.offerController = offerController;
        this.addContent(this.content());
    }
    VerticalLayout content() {

        // Grid
        Grid<OfferDTO> grid = new Grid<>(OfferDTO.class, false);
        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();
        ListDataProvider<OfferDTO> offerDataProvider = new ListDataProvider<>(offers);
        grid.setItems(offerDataProvider);

        //TODO: Fehler beim Zugreifen auf company deswegen in dto nur name übergeben -> keinen zugriff auf link
        /*grid.addColumn(item -> {
            Image image = new Image(item.getCompany().getImage(),
                    "");
            image.setWidth("50px");
            return image;
        }).setHeader("Logo").setWidth("50px");*/

        grid.addColumn(OfferDTO::getCompanyName).setHeader("Unternehmen").setWidth("150px");
        grid.addColumn(OfferDTO::getBusiness).setHeader("Branche").setWidth("250px"); // Ändern zu OfferDTO::getBusiness
        grid.addColumn(OfferDTO::getJob).setHeader("Stellenangebote").setWidth("500px"); // Ändern zu OfferDTO::getJob
        grid.addColumn(OfferDTO::getIncome).setHeader("€/h").setWidth("50px");

        // Filtering Components
        MultiSelectComboBox<String> businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(offers.stream().map(OfferDTO::getBusiness).distinct().collect(Collectors.toList()));
        TextField searchText = new TextField("Suchfeld", "Geben Sie hier einen Jobtitel oder ein Unternehmen ein..");
        NumberField minIncome = new NumberField("min ..€/h", "z.B. min 15€/h");

        // Filter - Job / Company
        searchText.setValueChangeMode(ValueChangeMode.EAGER);
        searchText.addValueChangeListener(event -> offerDataProvider.addFilter(
                offer -> {
                    if(StringUtils.containsIgnoreCase(offer.getCompanyName(), searchText.getValue()) ||
                            StringUtils.containsIgnoreCase(offer.getJob(), searchText.getValue())) {
                        return true;
                    } else {return false;}
                }));

        //Filter - minIncome
        minIncome.setValueChangeMode(ValueChangeMode.EAGER);
        minIncome.addValueChangeListener(event ->offerDataProvider.addFilter(
                offer -> {
                    try {
                        int income = 0;
                        try {
                            income = Integer.valueOf(offer.getIncome());
                        } catch (NumberFormatException e) {
                        }
                        if (income >= minIncome.getValue()) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch(NullPointerException e) {return true;}
                }));

        //Filter - Business
        businessComboBox.addValueChangeListener(event -> offerDataProvider.addFilter(
                offer -> {
                    if (businessComboBox.isEmpty()) {
                        return true;
                    } else {
                        return businessComboBox.isSelected(offer.getBusiness());
                    }
                }));
        businessComboBox.setClearButtonVisible(true);

        // Width, Hight, Icons
        grid.setWidth("1100px");
        minIncome.setWidth("150px");
        searchText.setWidth("770px");
        businessComboBox.setWidth("150px");
        searchText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

        RouterLink searchCompanyViewLink = new RouterLink(SearchCompaniesView.class);
        searchCompanyViewLink.setText("Nach Unternehmen suchen");

        // Layout
        return new VerticalLayout(
                new HorizontalLayout(
                        searchCompanyViewLink),
                new HorizontalLayout(
                        businessComboBox,
                        searchText,
                        minIncome),
                grid);

    }

}
