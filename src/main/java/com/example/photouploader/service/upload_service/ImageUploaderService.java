package com.example.photouploader.service.upload_service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ImageUploaderService {

    private Cloudinary cloudinary;
    private ImageRepo imageRepo;
    private ImageSaveService imageRepoSaver;
    private UserService userService;
    protected final Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    public ImageUploaderService(ImageRepo imageRepo, ImageSaveService imageRepoSaver, UserService userService) {
        this.userService = userService;
        this.imageRepoSaver = imageRepoSaver;
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "stalowy",
                "api_key", "934121649177224",
                "api_secret", "u2LS_LAFt2-gfjzGM1q6vxhieAY"));
    }

    public String uploadFile(File file) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            imageRepoSaver.saveImageToRepo(new Image(uploadResult.get("url").toString(), userService.getMyLoggedUserId()));
           // deleteTmpFile(uploadResult.get(10).toString() + "." + uploadResult.get(1).toString());
        } catch (IOException e) {
            //todo
        }
        return uploadResult.get("url").toString();

    }

    private void deleteTmpFile(String fileName){
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            log.warning(fileName + " not found");
        }
    }

}
