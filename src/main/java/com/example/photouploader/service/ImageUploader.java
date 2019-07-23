package com.example.photouploader.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public class ImageUploader {

    private Cloudinary cloudinary;
    private ImageRepo imageRepo;
    private ImageRepoSaver imageRepoSaver;

    @Autowired
    public ImageUploader(ImageRepo imageRepo, ImageRepoSaver imageRepoSaver) {
        this.imageRepoSaver = imageRepoSaver;
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "stalowy",
                "api_key", "119118156638564",
                "api_secret", "cSSpVZRIk43ruqJ-amyJMiTor28"));
    }

    public String uploadFile(File file) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            imageRepoSaver.saveImageToRepo(new Image(uploadResult.get("url").toString()));
        } catch (IOException e) {
            //todo
        }
        return uploadResult.get("url").toString();

    }
}
