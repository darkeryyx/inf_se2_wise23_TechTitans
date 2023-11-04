package group.artifact.views;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.*;
import group.artifact.controller.StudentController;
import group.artifact.entities.Application;
import group.artifact.entities.Student;
import group.artifact.repositories.StudentRepository;
import group.artifact.repositories.UserRepository;
import group.artifact.services.UserService;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import group.artifact.entities.User;
import group.artifact.controller.UserController;

import java.time.LocalDate;

@Route("student-profile")
public class CreateStudentProfileView extends Composite<Component> {

    @Autowired
    private StudentController controller;

    @Autowired
    private UserRepository userRepository;

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
                new Button("Bestätigen", buttonClickEvent -> controller.createStudentProfile(
                        new Student(
                        userRepository.getReferenceById(user.getValue().intValue()),
                        subject.getValue(),
                        birthday.getValue(),
                        semester.getValue().shortValue(),
                        skills.getValue(),
                        interests.getValue(),
                        description.getValue(),
                        image.getValue()
                ))
        ));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }
}
