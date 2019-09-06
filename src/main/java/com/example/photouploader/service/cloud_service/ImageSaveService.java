package com.example.photouploader.service.cloud_service;

import com.example.photouploader.model.Image;


public interface ImageSaveService {
    void saveImageToRepo(Image image);
}
