package com.example.photouploader.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = MainGui.ROUTE)
@PageTitle("Main")
public class MainGui extends VerticalLayout {
    public final static String ROUTE = "main";

    private Button registryButton = new Button(new RouterLink("Sign in", RegistrationGui.class));
    private Button loginButton = new Button(new RouterLink("Sign Up", LoginGui.class));
    private Div menu = new Div();

    @Autowired
    public MainGui() {
        menu.add(new HorizontalLayout(loginButton,registryButton));
        add(menu);
        add(new RouterLink("Home", LoginGui.class));
        add(new RouterLink("Homea", RegistrationGui.class));
        menu.setVisible(true);

//        loginButton.addClickListener(e -> {
//            UI.getCurrent().navigate(LoginGui.ROUTE);
//            UI.getCurrent().getPage().reload();
//        });
//        registryButton.addClickListener(e -> {
//            UI.getCurrent().navigate(RegistrationGui.ROUTE);
//            UI.getCurrent().getPage().reload();
//        });
    }
}
