package com.example.photouploader.service.upload_service;

import com.vaadin.flow.component.upload.SucceededEvent;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ByteConverter {
    public void byteArrayToFile(ByteArrayOutputStream baos, SucceededEvent event){
        ByteArrayOutputStream byteArrayOutputStream = baos;
        try(OutputStream outputStream = new FileOutputStream(event.getFileName())) {
            byteArrayOutputStream.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
