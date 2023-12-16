package group.artifact.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CompanyView extends BasisView{

    CompanyView() {
        super();
        menuBar.setEnabled(true);

        this.getStyle().set("padding","0");
        this.setAlignItems(Alignment.CENTER);
        this.addViews();
    }

}
