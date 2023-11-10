package group.artifact.views;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.*;

import com.vaadin.flow.data.validator.IntegerRangeValidator;
import group.artifact.entities.Student;
import group.artifact.controller.StudentController;
import group.artifact.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.LocalDate;

@Route("create/student")
public class CreateStudentProfileView extends VerticalLayout {

    @Autowired
    private StudentController studentController;
    @Autowired
    UserRepository userRepo;

    TextField subject = new TextField("Studienfach");
    DatePicker birthday = new DatePicker("Geburtsdatum");
    IntegerField semester = new IntegerField("Semester");
    TextField skills = new TextField("mitgebrachte Fähigkeiten");
    TextField interests = new TextField("Interessen");
    TextField description = new TextField("Beschreibung");
    TextField image = new TextField("Bild");
    IntegerField userId = new IntegerField("User ID (wird später entfernt)");
    Button submitButton = new Button("submit");
    Binder<Student> binder = new Binder<>(Student.class);

    public CreateStudentProfileView() {
        setSizeFull();
        add(buildForm());
        setUpBinder();
    }

    private Component buildForm() {
        setUpSubmitButton();
        VerticalLayout formLayout = new VerticalLayout(subject,
                birthday,
                semester,
                skills,
                interests,
                description,
                image,
                userId,
                submitButton);

        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return formLayout;
    }

    private void setUpSubmitButton() {
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(e -> createStudent());
    }

    private void createStudent() {
        if (userId.isEmpty() || userRepo.findById(userId.getValue()).isEmpty()) {
            Notification.show("Bitte gebe eine korrekte UserID an");
            return;
        } /*workaround: cannot bind integer user to user user, check with binder not possible */
        Student newStudent = new Student();
        try {
            binder.writeBean(newStudent);
            studentController.createStudentProfile(newStudent, userId.getValue());
            Notification.show("Studentenprofil erfolgreich erstellt!");
        } catch (ValidationException e) {
            Notification.show("Bitte überprüfen Sie Ihre Eingaben.");
        } catch (DataIntegrityViolationException e) {
            Notification.show("Für diese ID existiert bereits ein Profil");
        }
    }

    private void setUpBinder() {
        binder.bindInstanceFields(this);
        binder.forField(semester).
                asRequired("Semester ist ein Pflichtfeld").
                withValidator(new IntegerRangeValidator("Zahl muss zwischen 1 bis 99 liegen", 1, 99)).bind("semester");


        binder.forField(birthday)
                .withValidator(date ->
                        date.isBefore(LocalDate.now().plusDays(1)),
                "Datum kann nicht in der Zukunft liegen").
                withValidator(date ->
                        date.getYear() >= 1900, "Datum muss ab/nach 1900 liegen").
                bind("birthday");

        binder.forField(subject).
                withValidator(name -> name.length() >= 3,
                        "Das Studienfach muss mindestens 3 Buchstaben haben").bind("subject");

        binder.forField(subject).asRequired("Studienfach ist ein Plichtfeld");
        binder.forField(userId).asRequired("UserID ist ein Pflichtfeld");
    }
}