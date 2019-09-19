package com.example.photouploader.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = MainGui.ROUTE)
@PageTitle("Main")
@StyleSheet("MainGuiStyle.css")
public class MainGui extends VerticalLayout implements BeforeEnterListener {
    public final static String ROUTE = "main";
    public final static String LOGOUT_ROUTE = "logout";

    private Button registryButton = new Button("Sing Up");
    private Button loginButton = new Button("Sign In");
    private Div outer = new Div();
    private Div middle = new Div();
    private Div topDiv = new Div();
    private Div bottomDiv = new Div();
    private Div buttonsDiv = new Div();
    private H1 titleText = new H1("Photo Cloud");
    private Label descriptionText = new Label("Free cloud service for photos");

    @Autowired
    public MainGui() {
        //UI.getCurrent().getPage().addStyleSheet("/MainGuiStyle.css");
        topDiv.addClassName("topDiv");
        bottomDiv.addClassName("bottomDiv");
        outer.addClassName("outer");
        middle.addClassName("middle");
        titleText.addClassName("title");
        loginButton.addClassName("button");
        registryButton.addClassName("button");
        buttonsDiv.addClassName("buttonsDiv");
        descriptionText.addClassName("descriptionText");
        //escriptionText.getElement().getStyle().set("color","#ACCEFB");
        buttonsDiv.add(new HorizontalLayout(loginButton, registryButton));
        topDiv.add(new VerticalLayout(titleText, descriptionText));
        bottomDiv.add(buttonsDiv);
        outer.add(topDiv, bottomDiv);
        add(outer);

        loginButton.addClickListener(e -> {
            UI.getCurrent().navigate(LoginGui.ROUTE);
        });

        registryButton.addClickListener(e -> {
            UI.getCurrent().navigate(RegistrationGui.ROUTE);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println(beforeEnterEvent.getNavigationTarget());
    }

}
