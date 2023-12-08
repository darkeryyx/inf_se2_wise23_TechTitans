package group.artifact.views;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import group.artifact.controller.CompanyController;
import group.artifact.controller.UserController;
import group.artifact.dtos.CompanyDTO;
import group.artifact.dtos.UserDTO;
import group.artifact.dtos.impl.UserDTOImpl;
import group.artifact.entities.Company;
import group.artifact.entities.User;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;

import static org.springframework.data.support.PageableExecutionUtils.getPage;

@Route("view/companyProfile")
@RolesAllowed("ROLE_USER")
public class ViewCompanyProfile extends VerticalLayout {

    @Autowired
    private CompanyController companyController;

    @Autowired
    private UserController userController;
    TextField name = createTextField("Name");
    TextField business = createTextField("Branche");
    DatePicker founded = createDatePicker("Gründungsdatum");
    IntegerField employees = createIntegerField("Mitarbeiter");
    TextField link = createTextField("Website");
    TextField description = createTextField("Beschreibung");
    Image image;

    UploadFileFormat singleFileUpload = new UploadFileFormat();

    Binder<CompanyDTO> binder = new Binder<>(CompanyDTO.class);

    VerticalLayout uploadLayout = new VerticalLayout(singleFileUpload.title, singleFileUpload.hint,
            singleFileUpload.upload);

    Button editButton = new Button("Bearbeiten");
    Button saveButton = new Button("Speichern");

    public ViewCompanyProfile(UserController userController, CompanyController companyController) {
        this.companyController = companyController;
        this.userController = userController;
        setSizeFull();
        add(buildForm());
        editButton.addClickListener(e -> edit());
        saveButton.addClickListener(e -> save());
        saveButton.setVisible(false);
    }

    @PostConstruct
    public void init() {
        findCompany();
    }

    private Component buildForm() {

        image = generateImage();
        VerticalLayout formLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(
                new H2 ("Unternehmensprofil"));
        VerticalLayout profile = new VerticalLayout();
        uploadLayout.setAlignItems(Alignment.CENTER);
        uploadLayout.setVisible(false);
        if (image != null) {
            image.setWidth("50px");
            image.setHeight("50px");
            profile.add(
                    name,
                    business,
                    founded,
                    employees,
                    link,
                    description,
                    image);
        } else {
            profile.add(
                    name,
                    business,
                    founded,
                    employees,
                    link,
                    description);
        }
        profile.setAlignItems(Alignment.CENTER);
        formLayout.add(header,profile,uploadLayout, editButton, saveButton);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return formLayout;
    }


    public void findCompany() {
        User user = userController.getCurrentUser();
        if(!companyController.companyExists(user.getUser_pk())) {
            Notification.show("Für diesen User existiert kein Unternehmensprofil").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        CompanyDTO company = companyController.viewCompanyProfile(user.getUser_pk());
        binder.bindInstanceFields(this);
        binder.readBean(company);
    }

    public TextField createTextField(String s) {
        TextField tF = new TextField(s);
        tF.setReadOnly(true);
        return tF;
    }
    public IntegerField createIntegerField(String s) {
        IntegerField iF = new IntegerField(s);
        iF.setReadOnly(true);
        return iF;
    }

    public DatePicker createDatePicker(String s) {
        DatePicker dP = new DatePicker(s);
        dP.setReadOnly(true);
        return dP;
    }

    private void edit(){

        User user = userController.getCurrentUser();
        if ( user.getUser_pk() != null) {
            CompanyDTO companyDto = companyController.viewCompanyProfile( user.getUser_pk());
            if (companyDto != null) {
                binder.setBean(companyDto);
                setEditable(true);
                uploadLayout.setVisible(true);

            } else {
                Notification.show("Company nicht gefunden.");
            }
        } else {
            Notification.show("Bitte geben Sie eine gültige ID ein.");
        }

    }
    private void save() {
        User user = userController.getCurrentUser();
        try {
            CompanyDTO companyDto = binder.getBean();
            if (companyDto == null) {
                System.out.println("CompanyDto ist null!");
                Notification.show("Keine Daten zum Speichern.");
                return;
            }
            binder.writeBean(companyDto);

            companyController.updateCompanyProfile(companyDto, user.getUser_pk());
            Notification.show("Companyenprofil erfolgreich gespeichert.");

            setEditable(false);
            uploadLayout.setVisible(false);
            if(singleFileUpload.getValue() != null) {
                companyController.updateImage(userController.getCurrentUser().getUser_pk(),singleFileUpload.getValue());
                image = generateImage();
                UI.getCurrent().close();
            }
        } catch (ValidationException ex) {
            Notification.show("Validierungsfehler: " + ex.getMessage());
        }
    }


    private void setEditable(boolean editable) {
        saveButton.setVisible(editable);
        editButton.setVisible(!editable);

        name.setReadOnly(!editable);
        business.setReadOnly(!editable);
        founded.setReadOnly(!editable);
        employees.setReadOnly(!editable);
        link.setReadOnly(!editable);
        description.setReadOnly(!editable);
        singleFileUpload.setVisible(!editable);
        //image.setReadOnly(!editable);
    }
    public Image generateImage() {
        Optional<Company> c = companyController.findByID(userController.getCurrentUser().getUser_pk());
        Company company;
        if(c.isPresent()) {
            company = c.get();
        } else {
            company = null;
        }
        try {
        String enc = company.getImage();
        System.out.println("Bild in DB? "+ (enc!=null));
        if (enc==null){
            return null;
        }

        StreamResource sr = new StreamResource("company", () ->  {
            byte[] decoded = Base64.getDecoder().decode(enc);
            //byte [] arr = HexFormat.of().parseHex("89504e470d0a1a0a0000000d49484452000000050000000508060000008d6f26e50000001c4944415408d763f8ffff3fc37f062005c3201284d031f18258cd04000ef535cbd18e0e1f0000000049454e44ae426082");
            //System.out.println(HexFormat.of().parseHex("89504e470d0a1a0a0000000d49484452000000050000000508060000008d6f26e50000001c4944415408d763f8ffff3fc37f062005c3201284d031f18258cd04000ef535cbd18e0e1f0000000049454e44ae426082"));
            //System.out.println(new String(decoded));
            return new ByteArrayInputStream(decoded);
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, "Profilbild");

        return image;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Tag("div")
    public class UploadFileFormat extends AbstractSinglePropertyField<CreateProfile.UploadFileFormat, String> {

        String value; // The encoded image
        H4 title;
        Paragraph hint;
        Upload upload;

        public UploadFileFormat() {
            super("image", "", false);
            MemoryBuffer buffer = new MemoryBuffer();
            upload = new Upload(buffer);

            upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
            int maxFileSizeInBytes = 1024 * 1024; // 1MB
            upload.setMaxFileSize(maxFileSizeInBytes);

            upload.addFileRejectedListener(event -> {
                String errorMessage = event.getErrorMessage();

                Notification notification = Notification.show(errorMessage, 5000,
                        Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            });

            upload.addSucceededListener(event -> {
                InputStream fileData = buffer.getInputStream();
                System.out.println("succeeded");
                try {
                    // byte[] targetArray = new byte[fileData.available()];
                    String encoded = Base64.getEncoder().encodeToString(fileData.readAllBytes());
                    setValue(encoded);
                    // String encodedComp = Base64.getEncoder().encodeToString(targetArray);
                    System.out.println("Encoded " + encoded);

                    // String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            });

            upload.getElement().addEventListener("upload-abort", new DomEventListener() {
                @Override
                public void handleEvent(DomEvent domEvent) {
                    setValue(null);
                }
            });

            title = new H4("Bild hochladen");
            title.getStyle().set("margin-top", "0");
            hint = new Paragraph("Maximale Dateigröße: 1 MB. Akzeptierte Formate: jpeg, png, gif.");
            hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        }

        public void setValue(String v) {
            value = v;
        }

        public String getValue() {
            return value;
        }

    }
}

