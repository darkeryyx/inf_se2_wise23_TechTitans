package group.artifact.views;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;
import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.OfferDTO;
import group.artifact.repositories.CompanyRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("search/company")
@RolesAllowed("ROLE_USER")
public class SearchCompaniesView extends MainView {

    @Autowired
    CompanyRepository companyRepository;

    public SearchCompaniesView(){
        super();
        this.addContent(this.content());
    }

    VerticalLayout content() {

        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("Nach Stellenangebote suchen");

        //Grid

        /*List<CompanyDTO> companyDTOList = companyRepository.findAllCompanies();
        Grid<CompanyDTO> grid = new Grid<>(CompanyDTO.class, false);
        ListDataProvider<CompanyDTO> companyDataProvider = new ListDataProvider<>(companyDTOList);
        grid.setDataProvider(companyDataProvider);
        */
        Grid<CompanyDTO> grid = new Grid<>(CompanyDTO.class, false);

        grid.addColumn(item -> {
            Image image = new Image(item.getLogo(),
                    "");
            image.setWidth("100px");
            return image;
        }).setHeader("Logo").setWidth("100px");
        grid.addColumn(CompanyDTO:: getName ).setHeader("Unternehmen").setWidth("600px");
        grid.addColumn(CompanyDTO:: getLink).setHeader("Link").setWidth("160px");

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
