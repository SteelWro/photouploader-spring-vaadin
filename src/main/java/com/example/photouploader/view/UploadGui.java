package com.example.photouploader.view;

import com.example.photouploader.service.ByteConverter;
import com.example.photouploader.service.ImageUploader;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Route("uploadImage")
public class UploadGui extends VerticalLayout{

    private ImageUploader imageUploader;
    private ByteConverter byteConverter;

    @Autowired
    public UploadGui(ImageUploader imageUploader, ByteConverter byteConverter) {
        this.byteConverter = byteConverter;
        this.imageUploader = imageUploader;

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(event.getFileName()), event);
            imageUploader.uploadFile(new File(event.getFileName()));
        });

        add(upload);
    }




}


