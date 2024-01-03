package group.artifact.views;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import group.artifact.controller.*;
import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.OfferDTO;
import group.artifact.dtos.StudentDTO;
import group.artifact.dtos.UserDTO;
import group.artifact.entities.Company;
import group.artifact.entities.Message;
import group.artifact.entities.PostBox;
import group.artifact.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;


import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("search/offers")
@RolesAllowed("ROLE_USER")
public class SearchOffersView extends StudentView {

    private final OfferController offerController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private UserController userController;
    @Autowired
    private CompanyController companyController;
    @Autowired
    private PostBoxController postBoxController;
    @Autowired
    private MessageController messageController;


    public SearchOffersView(OfferController offerController) {
        super();
        this.offerController = offerController;
        this.setContent(this.content());
        this.add();
    }
    VerticalLayout content() {

        // Grid
        Grid<OfferDTO> grid = new Grid<>(OfferDTO.class, false);
        List<OfferDTO> offers = offerController.getAllOffersAndTheirCompany();
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
                searchings,
                grid
        );

        layout.setAlignSelf(Alignment.END);
        layout.setAlignSelf(Alignment.CENTER, searchings,grid, viewTitle);
        layout.setSizeFull();
        layout.setHeightFull();
        return layout;

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
        infoLayout.add(new Button("Jetzt bewerben!", event -> apply(item)));


        
        //RowLayout
        HorizontalLayout layout = new HorizontalLayout(logoLayout, infoLayout);
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


    public void apply(OfferDTO item) {
        User user =userController.getCurrentUser();
        System.out.println(user.getName());
        //vorname,nachname, studienfach,bday,semester, fähigkeiten interssen,beschreibung
        StudentDTO studentDTO= studentController.viewStudentProfile(user.getUser_pk()); //
      //  item.
        System.out.println(studentDTO);
     //   Optional<Company> company= companyController.getByID(item.getId());
       // System.out.println(company);
   //    CompanyDTO companyDTO=companyController.viewCompanyProfile(item.getCompany());


        //anfrage ok so?
    //   UserDTO createrOfOffer= userController.getUserDTO(company.get().getUser_fk());



        // Erstellen Sie ein Dialogfenster
        Dialog applyDialog = new Dialog();
        applyDialog.setHeight("auto");

        Label angaben = new Label("Ihre Angaben: ");
        Label name = new Label("Name: " + studentDTO.getName() + " " +studentDTO.getSurname());
        Label geburtstag= new Label("Geburtsdatum: " + studentDTO.getBirthday());
        Label studienfach = new Label("Studienfach: " + studentDTO.getSubject());
        Label semester = new Label("Semester: " + studentDTO.getSemester());
        Label faehigkeiten=new Label("Fähigkeiten: "+ studentDTO.getSkills());
        Label interessen= new Label("Interessen: " + studentDTO.getInterests());
        Label beschreibung = new Label("Beschreibung: " + studentDTO.getDescription());

        // Eingabefeld für den Text
        TextArea textArea = new TextArea("Bewerbungstext");
        textArea.setWidth("100%");
        textArea.setHeight("350px");

        // Markieren Sie das Textfeld als Pflichtfeld
        textArea.setRequiredIndicatorVisible(true);

        // Bestätigungsbutton
        Button confirmButton = new Button("Bewerbung abschicken!", event -> {
            // Überprüfen Sie, ob der Text eingegeben wurde, bevor Sie fortfahren
            if (textArea.isEmpty()) {
                // Hier können Sie eine Benachrichtigung oder eine andere Behandlung für den leeren Text hinzufügen
                Notification.show("Bitte geben Sie einen Bewerbungstext ein");
            } else {
                String applicationText = textArea.getValue();
                //tmp messageID, zum testen, user_pk= ID??, nächster plan: auf die postbox vom user zugreifen und die nachricht da einfügen
                                                            //dann maybe über messagecontroller ne mehode send und in dieser wird die message in die pobox eingefügt
                                                            //die liste der pobox wird dann dem user angezeigt

                //plan: von dem user auf die postbox und die nachricht dadrin saven
                PostBox box=postBoxController.getPostBoxByID(user);
                System.out.println("HIER"+ box.getPostBoxID());
               // System.out.println(company.get().getUser_fk());
                if(box !=null) {//1, user.getUser_pk(), box.getUser().getUser_pk(),"test subject", applicationText, false, 42
               //     Message message = new Message(box,user.getUser_pk(),company.get().getUser_fk(),"betrreff", applicationText, false);
                 //   messageController.createMessage(message);
                    applyDialog.close();
                }//todo: else fall -> neue postbox createn
            }
        });

        //verticallayout ging iwie nicht, angaben wurden trzd horizonzal angezeigt
        HorizontalLayout angabenLayout = new HorizontalLayout(angaben,name,geburtstag,studienfach, semester, faehigkeiten, interessen, beschreibung);
        angabenLayout.setAlignItems(Alignment.STRETCH);

        // Abbruchbutton
        Button cancelButton = new Button("Abbrechen", event -> applyDialog.close());

        // Layout für die Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);

        // Layout für das gesamte Dialogfenster
        VerticalLayout dialogLayout = new VerticalLayout(angabenLayout,textArea,buttonLayout);

        applyDialog.add(dialogLayout);

        // Dialogfenster anzeigen
        applyDialog.open();
    }
}
