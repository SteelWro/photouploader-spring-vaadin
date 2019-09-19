package com.example.photouploader.service.cloudinary_service;

import java.io.File;


public interface ImageUploaderService {
    String uploadFile(File file, long userId);

    void deleteTmpFile(String fileName);

}
