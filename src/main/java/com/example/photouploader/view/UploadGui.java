package com.example.photouploader.view;

import com.example.photouploader.service.ImageUploader;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("uploadImage")
public class UploadGui extends VerticalLayout {

    private ImageUploader imageUploader;

    @Autowired
    public UploadGui(ImageUploader imageUploader) {
        this.imageUploader = imageUploader;
        TextField textField = new TextField("link");
        Button button = new Button("upload");
        button.addClickListener(buttonClickEvent -> imageUploader.uploadFile(textField.getValue()));

        add(textField);
        add(button);
    }




}
