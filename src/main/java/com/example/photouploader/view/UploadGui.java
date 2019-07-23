package com.example.photouploader.view;

import com.example.photouploader.service.ImageUploader;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Route("uploadImage")
public class UploadGui extends VerticalLayout{

    private ImageUploader imageUploader;

    @Autowired
    public UploadGui(ImageUploader imageUploader) {
        this.imageUploader = imageUploader;
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setId("upload");

        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(),
                    buffer.getInputStream(event.getFileName()));
            add(component);
            System.out.println("udało się");
        });

        add(upload);
    }

    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
//        if (!mimeType.startsWith("")) {
//            throw new IllegalStateException();
//        }
        String text = "";
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        Div div = new Div();
        div.setText(text);
        div.addClassName("uploaded-text");
        return div;
    }
}


