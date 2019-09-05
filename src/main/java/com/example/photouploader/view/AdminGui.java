package com.example.photouploader.view;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.model.Role;
import com.example.photouploader.service.security_service.UserService;
import com.example.photouploader.service.upload_service.ByteConverter;
import com.example.photouploader.service.upload_service.ImageUploaderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Push
@Route(value = AdminGui.ROUTE)
@PageTitle("Admin Page")
@Secured(Role.ADMIN)
public class AdminGui extends VerticalLayout {
    public static final String ROUTE = "adminis";

    private ImageRepo imageRepo;
    private ImageUploaderService imageUploaderService;
    private ByteConverter byteConverter;
    private UserService userService;
    private Div gallery;
    private Div page2;

    @Autowired
    public AdminGui(ImageUploaderService imageUploaderService, ByteConverter byteConverter, UserService userService, ImageRepo imageRepo) {
        this.byteConverter = byteConverter;
        this.imageUploaderService = imageUploaderService;
        this.userService = userService;
        this.imageRepo = imageRepo;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        gallery = new Div();
        page2 = new Div();

        upload.addSucceededListener(event -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(event.getFileName()), event);
            imageUploaderService.uploadFile(new File(event.getFileName()),userService.getUserIdByUsername(authentication.getName()));
        });


        Tab tab1 = new Tab("Upload");
        Div page1 = new Div();
        page1.add(upload);

        Tab tab2 = new Tab("Gallery");
        page2.add(gallery);
        page2.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        Tabs tabs = new Tabs(tab1, tab2);
        Div pages = new Div(page1, page2);
        Set<Component> pagesShown = Stream.of(page1)
                .collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            updateGallery();
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            page2.add(gallery);
            pagesShown.add(selectedPage);

        });

        add(tabs, pages);

    }

    public void updateGallery(){
        HorizontalLayout horizontalLayout;
        gallery.removeAll();
        List<Image> images = imageRepo.findAll();
        List<com.vaadin.flow.component.html.Image> vaadinImages = new ArrayList<>();
        images.stream().forEach(element ->
        {
            vaadinImages.add(new com.vaadin.flow.component.html.Image(element.getImageAddress(), "cloudinaryPhoto"));
        });
        for(int i = 0; i<vaadinImages.size(); i++){
            gallery.add(vaadinImages.get(i));
        }

//        for(int i = 0; i<vaadinImages.size(); i++){
//            horizontalLayout = new HorizontalLayout();
//            for(int j = 0; j < 3; j++){
//                if(i<vaadinImages.size()) break;
//                horizontalLayout.add(vaadinImages.get(i));
//                i++;
//            }
//            gallery.add(horizontalLayout);
//            if(i<vaadinImages.size()) break;
//        }
    }




}


