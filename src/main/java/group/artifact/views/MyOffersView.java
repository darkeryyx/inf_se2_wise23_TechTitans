package group.artifact.views;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import group.artifact.controller.OfferController;
import group.artifact.controller.UserController;
import group.artifact.dtos.OfferDTO;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;


import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Route("my/offers")
@RolesAllowed("ROLE_USER")
public class MyOffersView extends HomeView {

    private final OfferController offerController;
    private final UserController userController;
    private VerticalLayout content;
    private VerticalLayout editContent;
    private int currentOffer;


    public MyOffersView(OfferController offerController, UserController userController) {
        super();
        this.currentOffer = 0;
        this.userController = userController;
        this.offerController = offerController;
        editContent = this.editContent();
        content = this.content();
        this.setContent(new VerticalLayout(this.content, this.editContent));
        this.add();
    }
    VerticalLayout content() {

        // Grid
        Grid<OfferDTO> grid = new Grid<>(OfferDTO.class, false);
        List<OfferDTO> offers = offerController.getAllOffersForOneCompany(userController.getCurrentUser().getUser_pk());
        ListDataProvider<OfferDTO> offerDataProvider = new ListDataProvider<>(offers);
        grid.setItems(offerDataProvider);

        grid.addComponentColumn( item-> gridRowLayout(item));

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
                        float income = 0;
                        try {
                            income = offer.getIncomePerHour();
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

        //Others
        RouterLink homeViewLink = new RouterLink(HomeView.class);
        homeViewLink.setText("-> Hier geht es zur HomeView");
        homeViewLink.getStyle().set("text-decoration", "underline");

        //Titel - erstmal vorläufig bis Standardheader definiert
        H3 viewTitle = new H3("Suchen und Filtern von Stellenangeboten");

        //Layout - searching and filtering
        businessComboBox.setWidth("15%");
        searchText.setWidth("85%");
        searchText.setPrefixComponent( new Icon (VaadinIcon.SEARCH));

        HorizontalLayout searchings = new HorizontalLayout(businessComboBox, searchText, minIncome);
        searchings.setWidth("70%");
        searchings.setHeight("10%");

        //Layout -grid
        grid.setWidth("70%");
        grid.setHeightByRows(true);
        grid.getStyle().set("border","none");
        grid.getStyle().set("box-shadow", "none");

        //Content Layout
        VerticalLayout layout = new VerticalLayout(
                viewTitle,
                homeViewLink,
                searchings,
                grid
        );

        layout.setAlignSelf(Alignment.END, homeViewLink);
        layout.setAlignSelf(Alignment.CENTER, searchings,grid, viewTitle);
        layout.setSizeFull();
        layout.setHeightFull();
        return layout;
    }

    VerticalLayout editContent() {
        //Edit
        TextField description = createTextField("Beschreibung");
        ComboBox<String> business = new ComboBox<>("Branche");
        NumberField income = new NumberField("Stundenlohn");
        TextField job = createTextField("Jobbezeichnung");
        List<String> businessList = offerController.getBusinessList();
        ComboBox.ItemFilter<String> filter = (b, filterString) -> b
                .toLowerCase().startsWith(filterString.toLowerCase());
        business.setItems(filter, businessList);

        Button editOfferButton = new Button("Veröffentlichen", event -> EditOffer(
                job.getValue(),
                business.getValue(),
                description.getValue(),
                income.getValue().floatValue())
        );
        editOfferButton.addClickListener(e -> {
                UI.getCurrent().getPage().reload();
        });

        Button cancelEditButton = new Button("Abbruch", event -> {
            UI.getCurrent().getPage().reload();
        }
        );

        VerticalLayout layout = new VerticalLayout(
                new H2("Jobausschreibung bearbeiten"),
                job,
                business,
                description,
                income,
                editOfferButton,
                cancelEditButton);
        if (business.isInvalid() | job.isInvalid() | description.isInvalid() | income.isInvalid())
            Notification.show("Bitte füllen Sie das erforderliche Feld aus.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setVisible(false);
        return layout;
    }

    private void EditOffer(String job, String business, String description, float income) {
        offerController.editOffer(this.currentOffer,job,business,description,income);
        this.content.setVisible(true);
        this.editContent.setVisible(false);
    }


    HorizontalLayout gridRowLayout(OfferDTO item){

        Image logo = generateImage(offerController.getLogo(item.getId()));
        Label details = new Label("Branche: " + item.getBusiness() + "\u3000\u3000" + "Gehalt: " + item.getIncomePerHour() + " €/h");
        Label company = new Label(item.getCompanyName());
        Label job = new Label(item.getJob());
        Label description = new Label(item.getDescription());

        //LogoLayout
        logo.setWidth("100px");
        logo.setHeight("100px");


        HorizontalLayout logoLayout = new HorizontalLayout(logo);
        logoLayout.setMargin(true);

        //InfoLayout
        company.getStyle().set("font-size", "9pt");

        job.getStyle().set("font-size", "16pt");
        job.getStyle().set("font-weight", "bold");
        job.getStyle().set("text-decoration", "underline");
        job.getStyle().set("margin-top", "0");

        description.getStyle().set("font-size", "11pt");
        description.getStyle().set("overflow", "hidden");
        description.getStyle().set("text-overflow", "ellipsis");
        description.getStyle().set("max-height", "3em");
        description.setWhiteSpace(HasText.WhiteSpace.NORMAL)  ;

        VerticalLayout infoLayout = new VerticalLayout(company,job,details, description);
        infoLayout.setSpacing(false);
        infoLayout.setAlignItems(Alignment.START);
        infoLayout.getStyle().set("display", "flex"); //-> Default css nachtrag

        Button startEditButton = new Button("Edit", e -> {
            this.content.setVisible(false);
            this.editContent.setVisible(true);
            currentOffer = item.getId();
        });


        //RowLayout
        HorizontalLayout layout = new HorizontalLayout(logoLayout, infoLayout,startEditButton);
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.START);

        return layout;
    }
    public Image generateImage(String logo) {
        try {
            String enc = logo;
            if (enc == null) {
                return null;
            }
            StreamResource sr = new StreamResource("company", () -> {
                byte[] decoded = Base64.getDecoder().decode(enc);
                return new ByteArrayInputStream(decoded);
            });
            sr.setContentType("image/png");
            Image image = new Image(sr, "Profilbild");

            return image;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private TextField createTextField(String label) {
        TextField requiredTextField = new TextField(label);
        requiredTextField.setRequired(true); // Make required field
        requiredTextField.setErrorMessage("Bitte füllen Sie das erforderliche Feld aus.");
        requiredTextField.setWidth("20%");
        return requiredTextField;
    }
}
