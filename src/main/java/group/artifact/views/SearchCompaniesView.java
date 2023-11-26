package group.artifact.views;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;

import group.artifact.controller.CompanyController;
import group.artifact.dtos.CompanyDTO;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route("search/company")
@RolesAllowed("ROLE_USER")
public class SearchCompaniesView extends HomeView {
    CompanyController companyController;
    MultiSelectComboBox<String> businessComboBox;

    public SearchCompaniesView(CompanyController companyController){
        super();
        this.companyController = companyController;
        this.addContent(this.content());
    }

    VerticalLayout content() {

        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("Nach Stellenangebote suchen");

        //Grid
        List<CompanyDTO> companyDTOList = companyController.getAllCompanies();
        Grid<CompanyDTO> grid = new Grid<>(CompanyDTO.class, false);
        ListDataProvider<CompanyDTO> companyDataProvider = new ListDataProvider<>(companyDTOList);
        grid.setItems(companyDataProvider);

        grid.addColumn(item -> {
            Image image = new Image(item.getLogo(),
                    "company view logo");
            image.setWidth("100px");
            return image;
        }).setHeader("Logo").setWidth("100px");
        Grid.Column<CompanyDTO> nameColumn = grid
                .addColumn(CompanyDTO:: getName ).setHeader("Unternehmen").setWidth("600px");
        Grid.Column<CompanyDTO> businessColumn = grid
                .addColumn(CompanyDTO:: getBusiness ).setHeader("Branche").setWidth("600px");
        Grid.Column<CompanyDTO> linkColumn = grid
                .addColumn(CompanyDTO:: getLink).setHeader("Link").setWidth("160px");

        //Searching Parameters
        businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(companyController.findAllBusinesses()); //Funktion: getBusinesses gruppiert

        TextField searchField = new TextField("Suchfeld", "Geben Sie hier einen Firmennamen ein");
        searchField.setPrefixComponent( new Icon (VaadinIcon.SEARCH));

        //Filter - Name
        searchField.addValueChangeListener(event -> companyDataProvider.addFilter(
                company -> StringUtils.containsIgnoreCase(company.getName(),
                        searchField.getValue())));

        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        //Filter - Business
        businessComboBox.addValueChangeListener(event -> companyDataProvider.addFilter(
                company -> {
                    if (businessComboBox.isEmpty()) {
                        return true;
                    } else {
                        return businessComboBox.isSelected(company.getBusiness());
                    }
                    }));
        businessComboBox.setClearButtonVisible(true);

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
