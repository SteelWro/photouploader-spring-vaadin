package com.example.photouploader.service.cloud_service_impl;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.service.cloud_service.ImageSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageSaveServiceImpl implements ImageSaveService {

    ImageRepo imageRepo;

    @Autowired
    public ImageSaveServiceImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    public void saveImageToRepo(Image image) {
        imageRepo.save(image);
    }
}
