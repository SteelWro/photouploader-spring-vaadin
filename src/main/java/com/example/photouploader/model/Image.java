package com.example.photouploader.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;
    private String thumbnailAddress;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public Image() {
    }

    public Image(String imageAddress, Long userId, String thumbnailAddress) {
        this.thumbnailAddress = thumbnailAddress;
        this.imageAddress = imageAddress;
        this.userId = userId;
    }

    public String getThumbnailAddress() {
        return thumbnailAddress;
    }

    public void setThumbnailAddress(String thumbnailAddress) {
        this.thumbnailAddress = thumbnailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
