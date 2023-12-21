package group.artifact.views;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("home")
@RolesAllowed("ROLE_USER")
public class HomeView extends BasisView {

    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    HomeView() {
     super();
     this.add();
    }


}
