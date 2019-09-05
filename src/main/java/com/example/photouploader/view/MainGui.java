package com.example.photouploader.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = MainGui.ROUTE)
@PageTitle("Main")
@StyleSheet("public/css/style.css")
public class MainGui extends VerticalLayout implements BeforeEnterListener {
    public final static String ROUTE = "main";

    private Button registryButton = new Button("Sing Up");
    private Button loginButton = new Button("Sign In");
    private Div mainDiv = new Div();
    private Div titleDiv = new Div();
    private Div buttonsDiv = new Div();
    private H1 titleText = new H1("Photo Cloud");

    @Autowired
    public MainGui() {
        buttonsDiv.add(new HorizontalLayout(loginButton,registryButton));
        titleText.add();
        mainDiv.add(titleDiv,buttonsDiv);
        add(mainDiv);


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
