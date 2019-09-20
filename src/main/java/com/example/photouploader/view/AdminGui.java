package com.example.photouploader.view;

import com.example.photouploader.model.Role;
import com.example.photouploader.service.cloudinary_service.ByteConverter;
import com.example.photouploader.service.cloudinary_service.ImageUploaderService;
import com.example.photouploader.service.repo_service.ImageRepoService;
import com.example.photouploader.service.repo_service.UserRepoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Push
@Route(value = AdminGui.ROUTE)
@PageTitle("Admin Page")
@Secured(Role.ADMIN)
public class AdminGui extends VerticalLayout {
    public static final String ROUTE = "admin";

    private ImageUploaderService imageUploaderService;
    private ByteConverter byteConverter;
    private UserRepoService userRepoService;
    private ImageRepoService imageRepoService;
    private Div gallery;
    private Tab tab1;
    private Div page1;
    private Tab tab2;
    private Div page2;
    private Tabs tabs;
    private Div pages;
    private Set<Component> pagesShown;
    private Map<Tab, Component> tabsToPages;
    private Button logoutButton;
    private HttpServletRequest req;

    @Autowired
    public AdminGui(ImageUploaderService imageUploaderService, ByteConverter byteConverter, UserRepoService userRepoService, ImageRepoService imageRepoService, HttpServletRequest req) {
        this.req = req;
        this.imageRepoService = imageRepoService;
        this.byteConverter = byteConverter;
        this.imageUploaderService = imageUploaderService;
        this.userRepoService = userRepoService;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        gallery = new Div();
        tab1 = new Tab("Upload");
        page1 = new Div();
        tab2 = new Tab("Gallery");
        page2 = new Div();
        tabs = new Tabs(tab1, tab2);
        pages = new Div(page1, page2);
        pagesShown = Stream.of(page1).collect(Collectors.toSet());
        tabsToPages = new HashMap<>();
        logoutButton = new Button("logout");

        page1.add(upload);
        page2.add(gallery);
        page2.setVisible(false);
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        tabs.addSelectedChangeListener(e -> tabChanger());

        upload.addSucceededListener(e -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(e.getFileName()), e);
            imageUploaderService.uploadFile(new File(e.getFileName()), userRepoService.getUserIdByUsername(authentication.getName()));
        });

        logoutButton.addClickListener(e -> {
            SecurityContextHolder.clearContext();
            req.getSession(false).invalidate();
            UI.getCurrent().getSession().close();
            UI.getCurrent().getPage().reload();
        });

        add(new HorizontalLayout(tabs, logoutButton), pages);
    }

    private void updateGallery() {
        gallery.removeAll();
        List<Image> images = imageRepoService.getAllThumbnails();
        images.forEach(this::accept);
        page2.add(gallery);
    }

    private void deletePhoto(Optional<String> alt){
        imageRepoService.deletePhoto(Long.valueOf(alt.get()));
        updateGallery();
    }

    private void tabChanger(){
        if(tab1.isVisible()) {
            updateGallery();
        }
        pagesShown.forEach(page -> page.setVisible(false));
        pagesShown.clear();
        Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
        pagesShown.add(selectedPage);
    }

    private void accept(Image i) {
        Button button = new Button("delete");
        button.addClickListener(e -> {
            deletePhoto(i.getAlt());
        });
        gallery.add(new HorizontalLayout(i, button));
    }
}


