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
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;
import group.artifact.dtos.StudentDTO;
import group.artifact.dtos.UserDTO;
import group.artifact.dtos.impl.UserDTOImpl;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Route("view/student")
@RolesAllowed("ROLE_USER")
public class ViewStudentProfile extends VerticalLayout {

    @Autowired
    private StudentController studentController;

    @Autowired
    private UserController userController;
    TextField name = createTextField("Vorname");
    TextField surname = createTextField("Nachname");
    TextField email = createTextField("E-Mail");
    TextField subject = createTextField("Studienfach");
    DatePicker birthday = createDatePicker("Geburtsdatum");
    IntegerField semester = createIntegerField("Semester");
    TextField skills = createTextField("Mitgebrachte Fähigkeiten");
    TextField interests = createTextField("Interessen");
    TextField description = createTextField("Beschreibung");
    Image image;
    UploadFileFormat singleFileUpload = new UploadFileFormat();
    VerticalLayout uploadLayout = new VerticalLayout(singleFileUpload.title, singleFileUpload.hint,
            singleFileUpload.upload);

    Binder<StudentDTO> binder = new Binder<>(StudentDTO.class);

    Button editButton = new Button("Bearbeiten");
    Button saveButton = new Button("Speichern");

    public ViewStudentProfile(UserController userController, StudentController studentController) {
        this.studentController = studentController;
        this.userController = userController;
        setSizeFull();
        add(buildForm());
        editButton.addClickListener(e -> edit());
        saveButton.addClickListener(e -> save());
        saveButton.setVisible(false);
    }

    @PostConstruct
    public void init() {
        findStudent();
    }

    private Component buildForm() {

        image = generateImage();

        VerticalLayout formLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(
                new H2 ("Studentenprofil"));
        VerticalLayout profile = new VerticalLayout();
        uploadLayout.setAlignItems(Alignment.CENTER);
        uploadLayout.setVisible(false);
        if (image != null) {
            image.setWidth("50px");
            image.setHeight("50px");
            profile.add(
                    name,
                    surname,
                    email,
                    subject,
                    birthday,
                    semester,
                    skills,
                    interests,
                    description,
                    image);
        } else {
            profile.add(
                    name,
                    surname,
                    email,
                    subject,
                    birthday,
                    semester,
                    skills,
                    interests,
                    description);
        }
        profile.setAlignItems(Alignment.CENTER);
        formLayout.add(header,profile,uploadLayout, editButton, saveButton);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return formLayout;
    }


    public void findStudent() {
        User user = userController.getCurrentUser();
        if(!studentController.studentExists(user.getUser_pk())) {
            Notification.show("Für diesen User existiert kein Studentenprofil").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        StudentDTO student = studentController.viewStudentProfile(user.getUser_pk());
        UserDTO userDto = userController.getUserDTO(user.getUser_pk());
        student.setName(userDto.getName());
        student.setSurname(userDto.getSurname());
        student.setEmail(userDto.getEmail());
        binder.bindInstanceFields(this);
        binder.readBean(student);
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
            StudentDTO studentDto = studentController.viewStudentProfile( user.getUser_pk());
            if (studentDto != null) {
                binder.setBean(studentDto);
                setEditable(true);
                uploadLayout.setVisible(true);
            } else {
                Notification.show("Student nicht gefunden.");
            }
        } else {
            Notification.show("Bitte geben Sie eine gültige ID ein.");
        }

    }
    private void save() {
        User user = userController.getCurrentUser();
        try {
            StudentDTO studentDto = binder.getBean();
            if (studentDto == null) {
                System.out.println("StudentDto ist null!");
                Notification.show("Keine Daten zum Speichern.");
                return;
            }
            binder.writeBean(studentDto);

            UserDTO userDto = new UserDTOImpl();
            userDto.setName(studentDto.getName());
            userDto.setSurname(studentDto.getSurname());
            userDto.setEmail(studentDto.getEmail());

            studentController.updateStudentProfile(studentDto, user.getUser_pk());
            userController.updateUser(userDto, user.getUser_pk());
            Notification.show("Studentenprofil erfolgreich gespeichert.");

            setEditable(false);
            uploadLayout.setVisible(false);
            if(singleFileUpload.getValue() != null) {
                studentController.updateImage(userController.getCurrentUser().getUser_pk(),singleFileUpload.getValue());
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
        surname.setReadOnly(!editable);
        email.setReadOnly(!editable);
        subject.setReadOnly(!editable);
        birthday.setReadOnly(!editable);
        semester.setReadOnly(!editable);
        skills.setReadOnly(!editable);
        interests.setReadOnly(!editable);
        description.setReadOnly(!editable);
        //image.setReadOnly(!editable);
    }
    public Image generateImage() {
        Student student = userController.getCurrentUser().getStudent();
        String enc = student.getImage();
        System.out.println("Bild in DB? "+ (enc!=null));
        if (enc==null){
            return null;
        }

        StreamResource sr = new StreamResource("student", () ->  {
            byte[] decoded = Base64.getDecoder().decode(enc);
            return new ByteArrayInputStream(decoded);
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, "Profilbild");

        return image;
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

