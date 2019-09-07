package com.example.photouploader.service.cloud_service_impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.service.security_service.UserService;
import com.example.photouploader.service.cloud_service.ImageSaveService;
import com.example.photouploader.service.cloud_service.ImageUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.tools.java.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class ImageUploaderServiceImpl implements ImageUploaderService {

    private Cloudinary cloudinary;
    private ImageRepo imageRepo;
    private ImageSaveService imageRepoSaver;
    private UserService userService;
    protected final Logger log = Logger.getLogger(getClass().getName());
    private Environment environment;

    @Autowired
    public ImageUploaderServiceImpl(ImageRepo imageRepo, ImageSaveService imageRepoSaver, UserService userService, Environment environment) {
        this.environment = environment;
        this.userService = userService;
        this.imageRepoSaver = imageRepoSaver;
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getP,
                "api_key", "934121649177224",
                "api_secret", "u2LS_LAFt2-gfjzGM1q6vxhieAY"));
    }

    public String uploadFile(File file, long userId) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            imageRepoSaver.saveImageToRepo(new Image(uploadResult.get("url").toString(), userId));
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
