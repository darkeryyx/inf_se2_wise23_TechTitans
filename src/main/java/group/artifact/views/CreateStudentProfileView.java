package group.artifact.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.*;

import com.vaadin.flow.data.validator.IntegerRangeValidator;
import group.artifact.controller.CompanyController;
import group.artifact.controller.UserController;
import group.artifact.entities.Student;
import group.artifact.controller.StudentController;
import group.artifact.entities.User;
import javax.annotation.security.RolesAllowed;

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
@RolesAllowed("ROLE_USER")
public class CreateStudentProfileView extends VerticalLayout {

    @Autowired
    private StudentController studentController;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private UserController userController;

    TextField subject = new TextField("Studienfach");
    DatePicker birthday = new DatePicker("Geburtsdatum");
    IntegerField semester = new IntegerField("Semester");
    TextField skills = new TextField("mitgebrachte Fähigkeiten");
    TextField interests = new TextField("Interessen");
    TextField description = new TextField("Beschreibung");
    TextField image = new TextField("Bild");
    Button submitButton = new Button("Bestätigen");
    Button cancelButton = new Button ("Abbruch");
    Binder<Student> binder = new Binder<>(Student.class);


    public CreateStudentProfileView() {
        setSizeFull();
        add(buildForm());
        setUpBinder();
    }

    private Component buildForm() {
        setUpSubmitButton();
        setUpCancelButton();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(submitButton, cancelButton);
        VerticalLayout formLayout = new VerticalLayout(
                new H2("Studentenprofil anlegen"),
                subject,
                birthday,
                semester,
                skills,
                interests,
                description,
                image,
                buttonLayout);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return formLayout;
    }

    private void setUpSubmitButton() {
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(e -> createStudent());
    }

    public void setUpCancelButton() {
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(e -> {
                binder.getFields().forEach(f -> f.clear());
                //UI.getCurrent().navigate("/login"); Wohin
                Notification.show("Profilerstellung abgebrochen!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }

    private void createStudent() {
        User user = userController.getCurrentUser();
        if (companyController.companyExists(user.getUser_pk())) {
            Notification.show("Für das Profil wurde schon ein Unternehmensprofil erstellt");
            return;
        }
        if (studentController.studentExists(user.getUser_pk())) {
            Notification.show("Das Studentprofil existiert bereits");
            UI.getCurrent().navigate("");
            return;
        } /*workaround: cannot bind integer user to user user, check with binder not possible */
        Student newStudent = new Student();
        try {
            binder.writeBean(newStudent);
            studentController.createStudentProfile(newStudent, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate(RegisterVerificationView.class);
            }));
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
                .withValidator(
                        date -> date == null || date.isBefore(LocalDate.now().plusDays(1)),
                        "Das Datum kann nicht in der Zukunft liegen"
                )
                .withValidator(
                        date -> date == null || date.getYear() >= 1900,
                        "Das Datum muss ab/nach 1900 liegen"
                )
                .bind("birthday");


        binder.forField(subject).
                withValidator(name -> name.length() >= 3,
                        "Das Studienfach muss mindestens 3 Buchstaben haben").bind("subject");

        binder.forField(subject).asRequired("Studienfach ist ein Plichtfeld");
    }
}