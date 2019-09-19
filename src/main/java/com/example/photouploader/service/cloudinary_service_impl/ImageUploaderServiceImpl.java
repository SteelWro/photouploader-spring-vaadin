package com.example.photouploader.service.cloudinary_service_impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.cloudinary.utils.ObjectUtils;
import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.service.cloudinary_service.ImageSaveService;
import com.example.photouploader.service.cloudinary_service.ImageUploaderService;
import com.example.photouploader.service.repo_service.ImageRepoService;
import com.example.photouploader.service.repo_service.UserRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ImageUploaderServiceImpl implements ImageUploaderService {

    private Cloudinary cloudinary;
    private ImageRepo imageRepo;
    private ImageRepoService imageRepoService;
    private UserRepoService userRepoService;
    protected final Logger log = Logger.getLogger(getClass().getName());
    private Environment environment;

    @Autowired
    public ImageUploaderServiceImpl(ImageRepo imageRepo, ImageRepoService imageRepoService, UserRepoService userRepoService, Environment environment) {
        this.environment = environment;
        this.userRepoService = userRepoService;
        this.imageRepoService = imageRepoService;
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getProperty("cloudinary.cloud_name"),
                "api_key", environment.getProperty("cloudinary.api_key"),
                "api_secret", environment.getProperty("cloudinary.api_secret")));
    }

    public String uploadFile(File file, long userId) {
        Map uploadResult = null;
        String thumbnailUrl = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            thumbnailUrl = cloudinary.url().transformation(new Transformation().width("200").height("200").crop("pad")).imageTag(uploadResult.get("public_id").toString() + "." + uploadResult.get("format")).substring(10,99);
            imageRepoService.saveImage(new Image(uploadResult.get("url").toString(), userId, thumbnailUrl));
            // deleteTmpFile(uploadResult.get(10).toString() + "." + uploadResult.get(1).toString());
        } catch (IOException e) {
            //todo
        }
        return uploadResult.get("url").toString();

    }

    public void deleteTmpFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            log.warning(fileName + " not found");
        }
    }
}
