package com.example.photouploader.service.upload_service;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageSaveService {

    ImageRepo imageRepo;

    @Autowired
    public ImageSaveService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    public void saveImageToRepo(Image image){
        imageRepo.save(image);
    }
}
