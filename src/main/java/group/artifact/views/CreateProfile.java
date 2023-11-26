package group.artifact.views;

import java.time.LocalDate;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.Route;

import group.artifact.controller.CompanyController;
import group.artifact.controller.StudentController;
import group.artifact.controller.UserController;
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
    private Button submitButton = new Button("Bestätigen");
    private Button skipButton = new Button("Überspringen", e -> UI.getCurrent().navigate("home"));

    // student fields
    TextField subject = new TextField("Studienfach");
    DatePicker birthday = new DatePicker("Geburtsdatum");
    IntegerField semester = new IntegerField("Semester");
    TextField skills = new TextField("mitgebrachte Fähigkeiten");
    TextField interests = new TextField("Interessen");
    TextField studentDescription = new TextField("Beschreibung");
    TextField image = new TextField("Bild");
    private Binder<Student> binder = new Binder<>(Student.class);

    // company fields
    IntegerField user = new IntegerField("User-ID");
    TextField name = new TextField("Firmenname");
    TextField business = new TextField("Branche");
    IntegerField employees = new IntegerField("Mitarbeiteranzahl");
    DatePicker founded = new DatePicker("Gründungsdatum");
    TextField link = new TextField("Link zur Unternehmenswebseite");
    TextField companyDescription = new TextField("Beschreibung");
    TextField logo = new TextField("Logo");

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

    private Component createStudentForm() {
        // create and return the student form component
        VerticalLayout studentForm = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, skipButton);
        studentForm.add(
                subject, birthday, semester, skills, interests, studentDescription, image, buttonLayout);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
            Notification.show("Das Studentprofil existiert bereits");
            UI.getCurrent().navigate("");
            return;
        }
        // workaround: cannot bind integer user to user, check with binder not possible
        Student newStudent = new Student();
        newStudent.setSkills(null);
        try {
            binder.writeBean(newStudent);
            studentController.createStudentProfile(newStudent, user);
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.navigate("/home");
            }));
            Notification.show("Studentenprofil erfolgreich erstellt!");
        } catch (ValidationException VE) {
            Notification.show("Bitte überprüfen Sie Ihre Eingaben.");
        } catch (DataIntegrityViolationException DIVE) {
            Notification.show("Für diese ID existiert bereits ein Profil");
        }
    }

    private void setUpStudentBinder() {
        binder.forField(skills).bind("skills");
        binder.forField(interests).bind("interests");
        binder.forField(studentDescription).bind("description");
        binder.forField(image).bind("image");

        binder.forField(semester).asRequired("Semester ist ein Pflichtfeld")
                .withValidator(new IntegerRangeValidator("Zahl muss zwischen 1 bis 99 liegen", 1, 99)).bind("semester");

        binder.forField(birthday)
                .withValidator(
                        date -> date == null || date.isBefore(LocalDate.now().plusDays(1)),
                        "Das Datum kann nicht in der Zukunft liegen")
                .withValidator(
                        date -> date == null || date.getYear() >= 1900,
                        "Das Datum muss ab/nach 1900 liegen")
                .bind("birthday");

        binder.forField(subject).withValidator(name -> name.length() >= 3,
                "Das Studienfach muss mindestens 3 Buchstaben haben").bind("subject");

        binder.forField(subject).asRequired("Studienfach ist ein Plichtfeld");
    }

    private Component createCompanyForm() {
        // create and return the company form component

        VerticalLayout companyForm = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, skipButton);
        companyForm.add(
                user, name, business, employees, founded, link, companyDescription, logo, buttonLayout);
        companyForm.setAlignItems(Alignment.CENTER);
        return companyForm;
    }
}
