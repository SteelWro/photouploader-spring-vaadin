package com.example.photouploader.service.security_service;

public interface UserService {
    Long getUserIdByUsername(String name);
    boolean isUserIsUsed(String username);
    void saveUser(String username, String password);
}
