package group.artifact.views;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route("create/profile")
@RolesAllowed("ROLE_USER")
public class CreateProfile extends VerticalLayout {

    public CreateProfile() {
        addClassName("create-profile-view");
        setSizeFull();

        Tab student = new Tab("Student");
        Tab company = new Tab("Company");
        Tabs tabs = new Tabs(student, company);

        HorizontalLayout tabsContainer = new HorizontalLayout(tabs);
        tabsContainer.setWidthFull();
        tabsContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        add(tabsContainer);
    }
}
