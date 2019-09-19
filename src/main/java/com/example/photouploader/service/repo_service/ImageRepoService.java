package com.example.photouploader.service.repo_service;

import com.example.photouploader.model.Image;

import java.util.List;

public interface ImageRepoService {
    void saveImage(Image image);
    String getThumbnailAddress(Long id);
    String getImageAddress(Long id);
    List<com.vaadin.flow.component.html.Image> getAllThumbnails();
    void deletePhoto(Long id);
}
