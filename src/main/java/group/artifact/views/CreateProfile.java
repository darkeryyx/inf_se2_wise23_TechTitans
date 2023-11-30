package group.artifact.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.component.html.Div;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.Route;

import group.artifact.controller.CompanyController;
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;
import group.artifact.entities.Company;
import group.artifact.entities.Student;
import group.artifact.entities.User;

@Route("create/profile")
@RolesAllowed("ROLE_USER")
@CssImport("./css/CreateProfileView.css")
public class CreateProfile extends VerticalLayout {

    @Autowired
    private StudentController studentController;
    @Autowired
    private CompanyController companyController;
    @Autowired
    private UserController userController;

    // general buttons
    private Component currentForm;
    private Button skipButton = new Button("Überspringen", e -> UI.getCurrent().navigate("home"));

    // student fields
    TextField subject = new TextField("Studienfach");
    DatePicker birthday = new DatePicker("Geburtsdatum");
    IntegerField semester = new IntegerField("Semester");
    TextField skills = new TextField("Mitgebrachte Fähigkeiten");
    TextField interests = new TextField("Interessen");
    TextField studentDescription = new TextField("Beschreibung");
    UploadFileFormat singleFileUpload = new UploadFileFormat();
    // UploadFileFormatTest uploadTest = new UploadFileFormatTest();
    private Binder<Student> studentBinder = new Binder<>(Student.class);

    // company fields
    TextField name = createRequiredTextField("Firmenname");
    TextField business = createRequiredTextField("Branche");
    IntegerField employees = createIntegerField("Mitarbeiteranzahl");
    DatePicker founded = createDatePicker("Gründungsdatum");
    TextField link = createRequiredTextField("Webseite");
    TextField logo = createTextField("Logo");
    TextArea companyDesription = createTextArea("Beschreibung");

    public CreateProfile() {
        addClassName("create-profile-view");
        setSizeFull();

        Tab student = new Tab("Student");
        Tab company = new Tab("Company");
        student.setClassName("tabs");
        company.setClassName("tabs");
        Tabs tabs = new Tabs(student, company);

        HorizontalLayout tabsContainer = new HorizontalLayout(tabs);
        tabsContainer.setWidthFull();
        tabsContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        add(tabsContainer);

        // initliaze the form with the student form
        currentForm = createStudentForm();
        add(currentForm);

        // add a selection listener to change the form based on the selected tab
        tabs.addSelectedChangeListener(event -> {
            remove(currentForm);
            if (event.getSelectedTab() == student) {
                currentForm = createStudentForm();
            } else if (event.getSelectedTab() == company) {
                currentForm = createCompanyForm();
            }
            add(currentForm);
        });
    }

    // student specific methods

    private Component createStudentForm() {
        // create and return the student form component
        Button submitButton = new Button("Bestätigen");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout studentForm = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, skipButton);
        VerticalLayout uploadLayout = new VerticalLayout(singleFileUpload.title, singleFileUpload.hint,
                singleFileUpload.upload);
        uploadLayout.setAlignItems(Alignment.CENTER);

        studentForm.add(
                subject, birthday, semester, skills, interests, studentDescription, uploadLayout, buttonLayout);
        submitButton.addClickListener(e -> createStudent());

        studentForm.setAlignItems(Alignment.CENTER);
        setUpStudentBinder();
        return studentForm;
    }

    private void createStudent() {
        User user = userController.getCurrentUser();
        if (companyController.companyExists(user.getUser_pk())) {
            Notification.show("Für das Profil wurde schon ein Unternehmensprofil erstellt");
            return;
        }
        if (studentController.studentExists(user.getUser_pk())) {
            Notification.show("Das Studentenprofil existiert bereits");
            return;
        }
        // workaround: cannot bind integer user to user, check with binder not possible
        Student newStudent = new Student();
        newStudent.setSkills(null);
        try {
            studentBinder.writeBean(newStudent);
            studentController.createStudentProfile(newStudent, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate("/home");
            }));
            Notification.show("Studentenprofil erfolgreich erstellt!");
        } catch (ValidationException VE) {
        } catch (DataIntegrityViolationException DIVE) {
            Notification.show("Für diese ID existiert bereits ein Profil");
        }
    }

    private void setUpStudentBinder() {
        studentBinder.forField(skills).bind("skills");
        studentBinder.forField(interests).bind("interests");
        studentBinder.forField(studentDescription).bind("description");
        studentBinder.forField(singleFileUpload).bind("image");
        // studentBinder.forField(uploadTest.getElement().getAttribute("value")).bind("image");

        studentBinder.forField(semester).asRequired("Semester ist ein Pflichtfeld")
                .withValidator(new IntegerRangeValidator("Zahl muss zwischen 1 und 99 liegen", 1, 99)).bind("semester");

        studentBinder.forField(birthday)
                .withValidator(
                        date -> date == null || date.isBefore(LocalDate.now().plusDays(1)),
                        "Das Datum kann nicht in der Zukunft liegen")
                .withValidator(
                        date -> date == null || date.getYear() >= 1900,
                        "Das Datum muss ab/nach 1900 liegen")
                .bind("birthday");

        studentBinder.forField(subject).withValidator(name -> name.length() >= 3,
                "Das Studienfach muss mindestens 3 Buchstaben haben").bind("subject");

        studentBinder.forField(subject).asRequired("Studienfach ist ein Plichtfeld");
    }

    // company specific methods

    private Component createCompanyForm() {
        // create and return the company form component
        Button submitButton = new Button("Bestätigen");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout companyForm = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, skipButton);
        VerticalLayout uploadLayout = new VerticalLayout(singleFileUpload.title, singleFileUpload.hint,
                singleFileUpload.upload);
        uploadLayout.setAlignItems(Alignment.CENTER);

        companyForm.add(
                name, business, employees, founded, link, logo, uploadLayout, companyDesription, buttonLayout);
        companyForm.setAlignItems(Alignment.CENTER);
        submitButton.addClickListener(event -> createCompany(
                name.getValue(),
                business.getValue(),
                employees.getValue(),
                founded.getValue(),
                link.getValue(),
                logo.getValue(),
                // singleFileUpload.getValue(),
                companyDesription.getValue()));

        return companyForm;
    }

    private void createCompany(String name, String business, Integer employees, LocalDate founded,
            String link, String logo, String description) {
        Company company = new Company(name, business, employees, founded, link, logo, description);
        try {
            User user = userController.getCurrentUser();
            companyController.createCompany(company, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate(HomeView.class);
            }));
            Notification.show("Firmenprofil erfolgreich angelegt.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (DataIntegrityViolationException DIVE) {
            Notification.show("Firmenprofil existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private TextField createTextField(String label) {
        TextField textField = new TextField(label);
        textField.setWidth("10%");
        return textField;
    }

    private DatePicker createDatePicker(String label) {
        DatePicker datepicker = new DatePicker(String.valueOf(label));
        datepicker.setWidth("10%");
        return datepicker;
    }

    private TextField createRequiredTextField(String label) {
        TextField requiredTextField = new TextField(label);
        requiredTextField.setRequired(true); // Make required field
        requiredTextField.setErrorMessage("Bitte füllen Sie das erforderliche Feld aus.");
        requiredTextField.setWidth("10%");
        return requiredTextField;
    }

    public TextArea createTextArea(String label) {
        TextArea textArea = new TextArea(label);
        textArea.setLabel(label);
        textArea.setHelperText("Beschreiben Sie Ihr Unternehmen kurz");
        textArea.setPlaceholder("Schreiben Sie hier . . . ");
        textArea.setWidth("30%");
        textArea.setHeight("30%");
        textArea.setClearButtonVisible(true);

        return textArea;

    }

    public IntegerField createIntegerField(String label) {
        IntegerField integerField = new IntegerField(label);
        integerField.setHelperText("mind. 1 Mitarbeiter(in)");
        integerField.setMin(1);
        integerField.setValue(1);
        integerField.setWidth("10%");
        integerField.setStepButtonsVisible(true);
        return integerField;
    }

    @Tag("div")
    public class UploadFileFormat extends AbstractSinglePropertyField<UploadFileFormat, String> {

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
                System.out.println(fileData);
                try {
                    // byte[] targetArray = new byte[fileData.available()];
                    String encoded = Base64.getEncoder().encodeToString(fileData.readAllBytes());
                    setValue(encoded);
                    // String encodedComp = Base64.getEncoder().encodeToString(targetArray);
                    System.out.println("Encoded " + encoded);

                    // String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
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

    /*
     * @Tag("div")
     * public class UploadFileFormatTest extends Component {
     * 
     * String value; //The encoded image
     * private Upload up;
     * 
     * public UploadFileFormatTest() {
     * MemoryBuffer buffer = new MemoryBuffer();
     * up = new Upload(buffer);
     * 
     * up.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
     * int maxFileSizeInBytes = 1024 * 1024; // 1MB
     * up.setMaxFileSize(maxFileSizeInBytes);
     * 
     * up.addFileRejectedListener(event -> {
     * String errorMessage = event.getErrorMessage();
     * 
     * Notification notification = Notification.show(errorMessage, 5000,
     * Notification.Position.MIDDLE);
     * notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
     * });
     * 
     * up.addSucceededListener(event -> {
     * InputStream fileData = buffer.getInputStream();
     * try {
     * String encoded = Base64.getEncoder().encodeToString(fileData.readAllBytes());
     * setValue(encoded);
     * getElement().setAttribute("value",getValue());
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * });
     * 
     * H4 title = new H4("Bild hochladen");
     * title.getStyle().set("margin-top", "0");
     * Paragraph hint = new
     * Paragraph("Maximale Dateigröße: 1 MB. Akzeptierte Formate: jpeg, png, gif.");
     * hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
     * 
     * add(title, hint, up);
     * }
     * public void setValue(String v){
     * value = v;
     * }
     * public String getValue() {
     * return value;
     * }
     * 
     * }
     */

}
