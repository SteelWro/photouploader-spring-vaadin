package com.example.photouploader.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = MainGui.ROUTE)
@PageTitle("Main")
public class MainGui extends VerticalLayout implements BeforeEnterListener {
    public final static String ROUTE = "main";

    private Button registryButton = new Button("Sing Up");
    private Button loginButton = new Button("Sign In");
    private Div menu = new Div();

    @Autowired
    public MainGui() {
        menu.add(new HorizontalLayout(loginButton,registryButton));
        add(menu);
        menu.setVisible(true);


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
