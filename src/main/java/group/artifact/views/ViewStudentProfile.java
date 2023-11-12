package group.artifact.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import group.artifact.controller.StudentController;
import group.artifact.dtos.StudentDTO;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
@Route("view/student")
@RolesAllowed("ROLE_USER")
public class ViewStudentProfile extends VerticalLayout {

    @Autowired
    private StudentController studentController;
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
    IntegerField search = new IntegerField("Suchfeld (Übergangslösung)", "Gebe eine UserID ein");
    Binder<StudentDTO> binder = new Binder<>(StudentDTO.class);
    Button searchButton = new Button("Suche");

    public ViewStudentProfile() {
        setSizeFull();
        add(buildForm());
    }

    private Component buildForm() {
        VerticalLayout formLayout = new VerticalLayout();
        createButton();
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
        formLayout.add(header,new VerticalLayout(search, searchButton),profile);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return formLayout;
    }

    public void createButton() {
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickListener(e -> {
            findStudent(search.getValue());
        });
    }

    public void findStudent(Integer id) {
        StudentDTO student = studentController.viewStudentProfile(id);
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
}

