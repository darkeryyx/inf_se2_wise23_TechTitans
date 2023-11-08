package group.artifact.views;

import group.artifact.entities.Student;
import group.artifact.controller.StudentController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.*;

@Route("student-profile")
public class CreateStudentProfileView extends Composite<Component> {

    @Autowired
    private StudentController studentController;

    protected Component initContent() {
        TextField subject = new TextField("Studienfach");
        DatePicker birthday = new DatePicker("Geburtsdatum");
        IntegerField semester = new IntegerField("Semester");
        TextField skills = new TextField("mitgebrachte Fähigkeiten");
        TextField interests = new TextField("Interessen");
        TextField description = new TextField("Beschreibung");
        TextField image = new TextField("Bild");
        IntegerField user = new IntegerField("User ID");

        VerticalLayout layout = new VerticalLayout(
                new H2("Studentenprofil anlegen"),
                user,
                subject,
                birthday,
                semester,
                skills,
                interests,
                description,
                image,
                new Button("Bestätigen", buttonClickEvent -> {
                        Student student = new Student(
                                subject.getValue(),
                                birthday.getValue(),
                                semester.getValue().shortValue(),
                                skills.getValue(),
                                interests.getValue(),
                                description.getValue(),
                                image.getValue());
                        try {
                            studentController.createStudentProfile(student, user.getValue().intValue());
                            Notification.show("Studentenprofil erfolgreich angelegt.")
                                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            ;
                        } catch (DataIntegrityViolationException DIVE) {
                            Notification.show("Studentenprofils existiert bereits ")
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                            ;
                        }
                    }));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }
}
