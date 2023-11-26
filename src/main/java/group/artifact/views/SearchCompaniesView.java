package group.artifact.views;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
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
import group.artifact.entities.Company;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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

        RouterLink searchOffersViewLink = new RouterLink(SearchOffersView.class);
        searchOffersViewLink.setText("->hier geht es zur Suche von Stellenangeboten<-");
        searchOffersViewLink.getStyle().set("text-decoration", "underline");

        //Grid
        List<CompanyDTO> companyDTOList = companyController.getAllCompanies();
        Grid<CompanyDTO> grid = new Grid<>(CompanyDTO.class, false);
        ListDataProvider<CompanyDTO> companyDataProvider = new ListDataProvider<>(companyDTOList);
        grid.setItems(companyDataProvider);

        Grid.Column<CompanyDTO> allInOneColumn = grid.addComponentColumn( item-> gridRowLayout(item));

        //Searching Parameters
        businessComboBox = new MultiSelectComboBox<>("Branchen");
        businessComboBox.setItems(companyController.findAllBusinesses()); //Funktion: getBusinesses gruppiert
        TextField searchField = new TextField("Suchfeld", "Geben Sie hier einen Firmennamen ein");

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

        //Layouts, Width and Hight
        searchField.setPrefixComponent( new Icon (VaadinIcon.SEARCH));
        HorizontalLayout searchings = new HorizontalLayout(businessComboBox, searchField);


        searchings.setWidth("70%");
        searchings.setHeight("10%");
        //---------------------------
        businessComboBox.setWidth("15%");
        searchField.setWidth("85%");


        grid.setWidth("70%");
        grid.setHeight("80%");
        //------------------------
        allInOneColumn.setWidth("50%");
        grid.setMaxHeight("90%");
        System.out.println(grid.getHeight());

        //Content Layout
        VerticalLayout layout = new VerticalLayout(
                searchOffersViewLink,
                searchings,
                grid
        );

        layout.setAlignSelf(Alignment.CENTER, searchings,grid);
        layout.setHeight("80%");
        System.out.println(layout.getHeight());
        return layout;
    }

    static HorizontalLayout gridRowLayout(CompanyDTO item){
        //Initialize
        Image logo = new Image (item.getLogo(), "images/fabrik.png");
        Anchor link = new Anchor(item.getLink(), item.getLink());
        Label name = new Label(item.getName());
        Label business = new Label(item.getBusiness());
        Label description = new Label(item.getDescription());

        VerticalLayout leftLayout = new VerticalLayout(logo);
        VerticalLayout middleLayout = new VerticalLayout(name, business, link);
        VerticalLayout rightLayout = new VerticalLayout(description);



        name.getStyle().set("font-size", "14pt");
        name.getStyle().set("font-weight", "bold");

        business.getStyle().set("font-size", "14pt");

        logo.getStyle().set("border-radius","2px");
        logo.setWidthFull();
        logo.setHeightFull();

        description.setWhiteSpace(HasText.WhiteSpace.NORMAL);

        link.getStyle().set("font-size","10pt");
        link.getStyle().set("font-weight", "bold");
        link.getStyle().set("max-width", "30%");
        link.setTarget("_blank");
        link.setWhiteSpace(HasText.WhiteSpace.NORMAL);

        leftLayout.setWidth("20%");
        middleLayout.setWidth("30%");
        rightLayout.setWidth("50%");

        return new HorizontalLayout(leftLayout,middleLayout,rightLayout);
    }

}
