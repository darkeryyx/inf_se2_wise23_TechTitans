package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;
import group.artifact.dtos.StudentDTO;
import group.artifact.entities.User;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

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
    TextField skills = createTextField("mitgebrachte Fähigkeiten");
    TextField interests = createTextField("Interessen");
    TextField description = createTextField("Beschreibung");
    TextField image = createTextField("Bild");

    Binder<StudentDTO> binder = new Binder<>(StudentDTO.class);

    Button editButton = new Button("Bearbeiten");
    Button saveButton = new Button("Speichern");

    public ViewStudentProfile() {
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

        VerticalLayout formLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(
                new H2 ("Studentenprofil"));
        VerticalLayout profile = new VerticalLayout(
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
        profile.setAlignItems(Alignment.CENTER);
        formLayout.add(header,profile, editButton, saveButton);
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
            studentController.updateStudentProfile(studentDto, user.getUser_pk());
            setEditable(false);
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
        image.setReadOnly(!editable);
    }
}

