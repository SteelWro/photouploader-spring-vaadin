package com.example.photouploader.view;

import com.example.photouploader.service.upload_service.ByteConverter;
import com.example.photouploader.service.upload_service.ImageUploaderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = AdminGui.ROUTE)
@PageTitle("Admin Page")
@Secured("ROLE_ADMIN")
public class AdminGui extends VerticalLayout{
    public static final String ROUTE = "adminis";

    private ImageUploaderService imageUploaderService;
    private ByteConverter byteConverter;

    @Autowired
    public AdminGui(ImageUploaderService imageUploaderService, ByteConverter byteConverter) {
        this.byteConverter = byteConverter;
        this.imageUploaderService = imageUploaderService;

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(event.getFileName()), event);
            imageUploaderService.uploadFile(new File(event.getFileName()));
        });

        Tab tab1 = new Tab("Upload");
        Div page1 = new Div();
        page1.add(upload);

        Tab tab2 = new Tab("Gallery");
        Div page2 = new Div();
        page2.setText("tu powstanie galeria obrazków");
        page2.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        Tabs tabs = new Tabs(tab1, tab2);
        Div pages = new Div(page1, page2);
        Set<Component> pagesShown = Stream.of(page1)
                .collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        add(tabs, pages);

    }




}


