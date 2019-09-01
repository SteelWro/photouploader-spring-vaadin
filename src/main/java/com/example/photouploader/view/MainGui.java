package com.example.photouploader.view;

import com.example.photouploader.service.ByteConverter;
import com.example.photouploader.service.ImageUploader;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = MainGui.ROUTE)
@PageTitle("Main")
@Secured("ROLE_ADMIN")
public class MainGui extends VerticalLayout{
    public static final String ROUTE = "main";

    private ImageUploader imageUploader;
    private ByteConverter byteConverter;

    @Autowired
    public MainGui(ImageUploader imageUploader, ByteConverter byteConverter) {
        this.byteConverter = byteConverter;
        this.imageUploader = imageUploader;

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(event.getFileName()), event);
            imageUploader.uploadFile(new File(event.getFileName()));
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


