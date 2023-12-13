package group.artifact.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CompanyView extends VerticalLayout{

    HorizontalLayout header;
    VerticalLayout content;
    HorizontalLayout fooder;

    CompanyView() {
    }

    CompanyView(VerticalLayout content) {
        header = new HorizontalLayout(

        );

        this.content = content;

        fooder = new HorizontalLayout(

        );
    }

    void setHeader(HorizontalLayout header) {
        this.header = header;
    }

    void setContent(VerticalLayout content) {
        this.content = content;
    }

    void setFooder(HorizontalLayout fooder) {
        this.fooder = fooder;
    }

    void add() {
        add(header, content, fooder);
    }
}
