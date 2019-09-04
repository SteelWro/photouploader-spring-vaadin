package com.example.photouploader.view;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = RegistrationGui.ROUTE)
@PageTitle("Registration")
public class RegistrationGui extends VerticalLayout {
    public final static String ROUTE = "registration";

    private FormLayout formLayout = new FormLayout();

    @Autowired
    public RegistrationGui() {
        TextField firstName = new TextField();
        formLayout.addFormItem(firstName, "Login");
        TextField lastName = new TextField();
        formLayout.addFormItem(lastName, "Password");

        add(formLayout);
    }
}
