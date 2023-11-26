package group.artifact.views;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import group.artifact.controller.OfferController;
import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.OfferDTO;
import group.artifact.entities.Company;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("search/offers")
@RolesAllowed("ROLE_USER")
public class SearchOffersView extends HomeView {

    private final OfferController offerController;
    public SearchOffersView(OfferController offerController) {
        super();
        this.offerController = offerController;
        this.setContent(this.content());
    }
    VerticalLayout content() {

        // Grid
        Grid<OfferDTO> grid = new Grid<>(OfferDTO.class, false);
        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();
        ListDataProvider<OfferDTO> offerDataProvider = new ListDataProvider<>(offers);
        grid.setItems(offerDataProvider);

        //TODO: Fehler beim Zugreifen auf company deswegen in dto nur name übergeben -> keinen zugriff auf link
        grid.addComponentColumn(item -> gridRowLayout(item));

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
        VerticalLayout layout = new VerticalLayout(
                new HorizontalLayout(
                        searchCompanyViewLink),
                new HorizontalLayout(
                        businessComboBox,
                        searchText,
                        minIncome),
                grid);

        return layout;
    }
    static HorizontalLayout gridRowLayout(OfferDTO item){

        Image logo = new Image ("images/fabrik.png", "images/fabrik.png");
        Label companyName = new Label(item.getCompanyName());
        Label business = new Label(item.getBusiness());
        Label job = new Label(item.getJob());
        Label description = new Label(item.getDescription());
        Label income = new Label(item.getIncome());


        //left
        VerticalLayout leftLayout = new VerticalLayout(logo, business);

        logo.getStyle().set("border-radius","2px");
        logo.setWidthFull();
        logo.setHeightFull();

        business.getStyle().set("font-size", "14pt");

        //Middle
        VerticalLayout rightLayout = new VerticalLayout(companyName, job, description);
        companyName.getStyle().set("font-size", "10pt");

        job.getStyle().set("font-size", "14pt");
        job.getStyle().set("font-weight", "bold");
        description.getStyle().set("font-size", "11pt");

        description.setWhiteSpace(HasText.WhiteSpace.NORMAL);

        
        leftLayout.setWidth("30%");
        rightLayout.setWidth("70%");

        return new HorizontalLayout(leftLayout, rightLayout);
    }

}
