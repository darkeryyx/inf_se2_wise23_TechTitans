package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;
import group.artifact.dtos.StudentDTO;
import group.artifact.entities.Student;
import group.artifact.entities.User;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

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

    public ViewStudentProfile(UserController userController, StudentController studentController) {
        this.studentController = studentController;
        this.userController = userController;
        setSizeFull();
        add(buildForm());}

    @PostConstruct
    public void init() {
        findStudent();
    }

    private Component buildForm() {

        //Image testImage = generateImage(userController.getCurrentUser());
        Image testImage = generateImage();

        if (testImage != null) {
            image.setWidth("75px");
            image.setHeight("75px");
        }

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
                //image,
                testImage);
        profile.setAlignItems(Alignment.CENTER);
        formLayout.add(header,profile);
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
    public Image generateImage() {
        //Der übergebene User ist der aktuelle User
        Student student = userController.getCurrentUser().getStudent();
        //String decoded = new String(Base64.getDecoder().decode(img.getBytes()));
        String enc = student.getImage();
        //String enc = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";

        StreamResource sr = new StreamResource("student", () ->  {
            //Student student = user.getStudent();
            //String img = student.getImage();
            //String decoded = new String(Base64.getDecoder().decode(img.getBytes()));
            byte[] decoded = Base64.getDecoder().decode(enc);
            //byte [] arr = HexFormat.of().parseHex("89504e470d0a1a0a0000000d49484452000000050000000508060000008d6f26e50000001c4944415408d763f8ffff3fc37f062005c3201284d031f18258cd04000ef535cbd18e0e1f0000000049454e44ae426082");
            //System.out.println(HexFormat.of().parseHex("89504e470d0a1a0a0000000d49484452000000050000000508060000008d6f26e50000001c4944415408d763f8ffff3fc37f062005c3201284d031f18258cd04000ef535cbd18e0e1f0000000049454e44ae426082"));
            //System.out.println(new String(decoded));
            return new ByteArrayInputStream(decoded);
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, "Profilbild");

        return image;
    }
}

