package com.example.photouploader.model;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;

    @Column(name = "car_id", nullable = false)
    private Long carId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user;

    public Image() {
    }

    public Image(String imageAddress, Long carId) {
        this.imageAddress = imageAddress;
        this.carId = carId;
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
