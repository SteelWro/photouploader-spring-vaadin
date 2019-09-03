package com.example.photouploader.service.upload_service_impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.service.security_service.UserService;
import com.example.photouploader.service.upload_service.ImageSaveService;
import com.example.photouploader.service.upload_service.ImageUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ImageSaveService imageRepoSaver;
    private UserService userService;
    protected final Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    public ImageUploaderServiceImpl(ImageRepo imageRepo, ImageSaveService imageRepoSaver, UserService userService) {
        this.userService = userService;
        this.imageRepoSaver = imageRepoSaver;
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "stalowy",
                "api_key", "934121649177224",
                "api_secret", "u2LS_LAFt2-gfjzGM1q6vxhieAY"));
    }

    /**
     * Uploading file to external Service and save url to database
     *
     * @param file uploaded file
     * @return url uploaded file from External Service
     */
    public String uploadFile(File file, long userId) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            imageRepoSaver.saveImageToRepo(new Image(uploadResult.get("url").toString(),userId));
            // deleteTmpFile(uploadResult.get(10).toString() + "." + uploadResult.get(1).toString());
        } catch (IOException e) {
            //todo
        }
        return uploadResult.get("url").toString();

    }

     public void deleteTmpFile(String fileName){
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            log.warning(fileName + " not found");
        }
    }
}
