package com.example.photouploader.view;

import com.example.photouploader.model.User;
import com.example.photouploader.service.repo_service.UserRepoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = RegistrationGui.ROUTE)
@PageTitle("Registration")
@StyleSheet("RegistrationGuiStyle.css")
public class RegistrationGui extends VerticalLayout {
    public final static String ROUTE = "registration";

    UserRepoService userRepoService;

    FormLayout layoutWithBinder = new FormLayout();
    Binder<User> binder = new Binder<>();
    User newUser = new User();
    Label labelText = new Label("Create your user");
    TextField loginField = new TextField("login");
    PasswordField passwordField = new PasswordField("password");
    Dialog good = new Dialog(new Label("good"));
    Dialog bad = new Dialog(new Label("bad"));
    Div outer = new Div();
    Div inner = new Div();
    Button save = new Button("Save");


    @Autowired
    public RegistrationGui(UserRepoService userRepoService) {
        this.userRepoService = userRepoService;
        layoutWithBinder.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));
        layoutWithBinder.addFormItem(loginField, loginField);
        layoutWithBinder.addFormItem(passwordField, passwordField);
        layoutWithBinder.setResponsiveSteps();

        loginField.setRequiredIndicatorVisible(true);
        passwordField.setRequiredIndicatorVisible(true);

        SerializablePredicate<String> phoneOrEmailPredicate = value -> !loginField
                .getValue().trim().isEmpty()
                || !passwordField.getValue().trim().isEmpty();

        Binder.Binding<User, String> loginBinding = binder.forField(loginField)
                .withValidator(new StringLengthValidator(
                        "login from 3 to 32 characters", 3, 32))
                .bind(User::getUsername, User::setUsername);

        binder.forField(loginField)
                .withValidator((Validator<String>) (value, valueContext) -> {
                    Boolean boo = !userRepoService.isUserIsUsed(value);
                    if (boo) {
                        return ValidationResult.ok();
                    }
                    return ValidationResult.error("Login in used");
                })
                .bind(User::getUsername, User::setUsername);

        Binder.Binding<User, String> passwordBinding = binder.forField(passwordField)
                .withValidator(new StringLengthValidator(
                        "Password minimum length is 6 characters", 6, 120))
                .bind(User::getPassword, User::setPassword);

        loginField.addValueChangeListener(e -> loginBinding.validate());
        passwordField.addValueChangeListener(e -> passwordBinding.validate());

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(newUser)) {
                good.open();
                userRepoService.saveUser(loginField.getValue(), passwordField.getValue());
            } else {
                BinderValidationStatus<User> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                bad.open();
            }
        });

        outer.addClassName("outer");
        inner.addClassName("inner");
        labelText.addClassName("label");
        save.addClassName("primary");
        inner.add(layoutWithBinder, save);
        outer.add(labelText);
        outer.add(inner);
        add(outer);
    }
}
