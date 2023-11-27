package group.artifact.views;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
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



@Route("search/companies")
@RolesAllowed("ROLE_USER")
public class SearchCompaniesView extends HomeView {
    CompanyController companyController;
    MultiSelectComboBox<String> businessComboBox;

    public SearchCompaniesView(CompanyController companyController){
        super();
        this.companyController = companyController;
        this.setContent(this.content());
        this.add();
    }

    VerticalLayout content() {

        //Grid
        List<CompanyDTO> companyDTOList = companyController.getAllCompanies();
        Grid<CompanyDTO> grid = new Grid<>(CompanyDTO.class, false);
        ListDataProvider<CompanyDTO> companyDataProvider = new ListDataProvider<>(companyDTOList);
        grid.setItems(companyDataProvider);
        grid.addComponentColumn( item-> gridRowLayout(item));

        //Searching Parameters
        businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(companyController.findAllBusinesses()); //Funktion: getBusinesses gruppiert
        TextField searchField = new TextField("Suchfeld", "Geben Sie hier einen Firmennamen ein..");

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


        //Layout - searching and filtering
        businessComboBox.setWidth("15%");
        searchField.setWidth("85%");
        searchField.setPrefixComponent( new Icon (VaadinIcon.SEARCH));

        HorizontalLayout searchings = new HorizontalLayout(businessComboBox, searchField);
        searchings.setWidth("70%");
        searchings.setHeight("10%");

        //Others
        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("-> hier geht es zur Suche von Stellenangeboten");
        searchOffersViewLink.getStyle().set("text-decoration", "underline");

        //Titel - erstmal vorläufig bis Standardheader definiert
        H3 viewTitle = new H3("Suchen und Filtern von Unternehmen");


        //Layout -grid
        grid.setWidth("70%");
        grid.setHeightByRows(true);
        grid.getStyle().set("border","none");
        grid.getStyle().set("box-shadow", "none");

        //Content Layout
        VerticalLayout layout = new VerticalLayout(
                viewTitle,
                searchOffersViewLink,
                searchings,
                grid
        );

        layout.setAlignSelf(Alignment.END, searchOffersViewLink);
        layout.setAlignSelf(Alignment.CENTER, searchings,grid, viewTitle);
        layout.setSizeFull();
        layout.setHeightFull();
        return layout;
    }

    static HorizontalLayout gridRowLayout(CompanyDTO item){

        Anchor link = new Anchor(item.getLink(), item.getLink());
        Image logo = new Image ("images/fabrik.png", "images/fabrik.png");
        Label business = new Label("Branche: " + item.getBusiness());
        Label company = new Label(item.getName());
        Label description = new Label(item.getDescription());

        //LogoLayout
        logo.setWidth("100px");
        logo.setHeight("100px");

        HorizontalLayout logoLayout = new HorizontalLayout(logo);
        logoLayout.setMargin(true);

        //InfoLayout
        company.getStyle().set("font-size", "16pt");
        company.getStyle().set("font-weight", "bold");
        company.getStyle().set("text-decoration", "underline");
        company.getStyle().set("margin-top", "0");

        description.getStyle().set("font-size", "11pt");
        description.getStyle().set("overflow", "hidden");
        description.getStyle().set("text-overflow", "ellipsis");
        description.getStyle().set("max-height", "3em");
        description.setWhiteSpace(HasText.WhiteSpace.NORMAL)  ;

        link.getStyle().set("font-size","10pt");
        link.getStyle().set("font-weight", "bold");
        link.setTarget("_blank");
        link.setWhiteSpace(HasText.WhiteSpace.NORMAL);

        VerticalLayout infoLayout = new VerticalLayout(company, business, description, link);
        infoLayout.setSpacing(false);
        infoLayout.setAlignItems(Alignment.START);

        //RowLayout
        HorizontalLayout layout = new HorizontalLayout(logoLayout, infoLayout);
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.START);

        return layout;
    }

}
