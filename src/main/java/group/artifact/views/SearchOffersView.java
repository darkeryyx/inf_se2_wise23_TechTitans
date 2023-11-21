package group.artifact.views;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import group.artifact.controller.OfferController;
import group.artifact.dtos.OfferDTO;
import group.artifact.entities.Company;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("search/offer")
@RolesAllowed("ROLE_USER")
public class SearchOffersView extends MainView {

    private final OfferController offerController;
    private OfferFilter offerFilter;
    private OfferDTODataProvider dataProvider;
    private ConfigurableFilterDataProvider<OfferDTO, Void, OfferFilter> filterDataProvider;

    // Constructor with OfferController as a dependency
    public SearchOffersView(OfferController offerController) {
        super();
        this.offerController = offerController;
        this.addContent(this.content());
        init();
    }

    @PostConstruct
    public void init() {
        offerFilter = new OfferFilter();
        dataProvider = new OfferDTODataProvider();
        filterDataProvider = dataProvider.withConfigurableFilter();
    }

    VerticalLayout content() {

        // Grid - Umändern siehe Klassenvariablen
        Grid<OfferDTO> grid = new Grid<>(OfferDTO.class, false);
        grid.setItems();
        grid.addColumn(item -> {
            Image image = new Image(item.getCompany().getImage(),
                    "");
            image.setWidth("50px");
            return image;
        }).setHeader("Logo").setWidth("50px");
        grid.addColumn(item -> item.getCompany().getName()).setHeader("Unternehmen").setWidth("150px");
        grid.addColumn(OfferDTO::getBusiness).setHeader("Branche").setWidth("50px"); // Ändern zu OfferDTO::getBusiness
        grid.addColumn(OfferDTO::getJob).setHeader("Stellenangebote").setWidth("700px"); // Ändern zu OfferDTO::getJob
        grid.addColumn(OfferDTO::getIncome).setHeader("€/h").setWidth("50px");

        // Filtering Components
        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();
        MultiSelectComboBox<String> businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(offers.stream().map(OfferDTO::getBusiness).distinct().collect(Collectors.toList()));
        TextField searchText = new TextField("Suchfeld", "Geben Sie hier einen Jobtitel oder ein Unternehmen ein..");
        NumberField minIncome = new NumberField("min ..€/h", "z.B. min 15€/h");

        // Filtering
        searchText.setValueChangeMode(ValueChangeMode.EAGER);
        searchText.addValueChangeListener(e -> {
            offerFilter.setSearchTerm(e.getValue());
            filterDataProvider.setFilter(offerFilter);
        });

        minIncome.setValueChangeMode(ValueChangeMode.EAGER);
        minIncome.addValueChangeListener(e -> {
            offerFilter.setSearchTerm(String.valueOf(e.getValue()));
            filterDataProvider.setFilter(offerFilter);
        });

        businessComboBox.addSelectionListener(event -> {
        }); // Filtern einfügen

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

    protected class OfferFilter {
        private String searchTerm;
        private Double minIncome;

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        public void setMinIncome(Double minIncome) {
            this.minIncome = minIncome;
        }

        public boolean test(OfferDTO offer) {
            Company company = offer.getCompany();
            boolean matchesFullName = matchesSearchTerm(company.getName(), searchTerm);
            boolean matchesProfession = matchesMinIncome(Double.parseDouble(offer.getIncome()), minIncome);
            return matchesFullName || matchesProfession;
        }

        private boolean matchesSearchTerm(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

        private boolean matchesMinIncome(Double value, Double minIncome) {
            return minIncome == null || minIncome.isNaN()
                    || value >= minIncome;
        }
    }

    private class OfferDTODataProvider extends AbstractBackEndDataProvider<OfferDTO, OfferFilter> {
        final List<OfferDTO> DATABASE = new ArrayList<>(offerController.getAllOffersAndTheirCompany());

        @Override
        protected Stream<OfferDTO> fetchFromBackEnd(Query<OfferDTO, OfferFilter> query) {
            // A real app should use a real database or a service
            // to fetch, filter and sort data.
            Stream<OfferDTO> stream = DATABASE.stream();

            // Filtering
            if (query.getFilter().isPresent()) {
                stream = stream.filter(offer -> query.getFilter().get().test(offer));
            }

            // Pagination
            return stream.skip(query.getOffset()).limit(query.getLimit());
        }

        @Override
        protected int sizeInBackEnd(Query<OfferDTO, OfferFilter> query) {
            return 0;
        }
    }

}
