package com.example.photouploader.service;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageRepoSaver {

    ImageRepo imageRepo;

    @Autowired
    public ImageRepoSaver(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    public void saveImageToRepo(Image image){
        imageRepo.save(image);
    }
}
