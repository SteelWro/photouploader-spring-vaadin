package com.example.photouploader.view;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


@Route(value = UserGui.ROUTE)
@PageTitle("Gallery")
@Secured("USER")
public class UserGui extends VerticalLayout {
    public static final String ROUTE = "gallery";

    ImageRepo imageRepo;

    @Autowired
    public UserGui(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
        List<Image> images = imageRepo.findAll();
        images.stream().forEach(element ->
        {
            com.vaadin.flow.component.html.Image image = new com.vaadin.flow.component.html.Image(element.getImageAddress(), "brak");
            add(image);
        });



    }
}
