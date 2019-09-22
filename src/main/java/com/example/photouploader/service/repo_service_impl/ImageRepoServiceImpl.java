package com.example.photouploader.service.repo_service_impl;

import com.example.photouploader.model.Image;
import com.example.photouploader.repo.ImageRepo;
import com.example.photouploader.service.repo_service.ImageRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageRepoServiceImpl implements ImageRepoService {
    ImageRepo imageRepo;

    @Autowired
    public ImageRepoServiceImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    private List<com.vaadin.flow.component.html.Image> getImages(List<Image> images) {
        List<com.vaadin.flow.component.html.Image> vaadinImages = new ArrayList<>();
        images.stream().forEach(e ->
        {
            vaadinImages.add(new com.vaadin.flow.component.html.Image(e.getThumbnailAddress(), e.getId().toString()));
        });
        return vaadinImages;
    }

    @Override
    public void saveImage(Image image) {
        imageRepo.save(image);
    }

    @Override
    public String getThumbnailAddress(Long id) {
       return imageRepo.getOne(id).getImageAddress();
    }

    @Override
    public String getImageAddress(Long id) {
        return imageRepo.getOne(id).getImageAddress();
    }

    @Override
    public List<com.vaadin.flow.component.html.Image> getAllThumbnails() {
        List<Image> images = imageRepo.findAll();
        return getImages(images);
    }

    @Override
    public List<com.vaadin.flow.component.html.Image> getAllThumbnailsById(Long id) {
        List<Image> images = (List<Image>) imageRepo.findAllById(id);
        return getImages(images);
    }

    @Override
    public void deletePhoto(Long id) {
        imageRepo.deleteById(id);
    }
}
