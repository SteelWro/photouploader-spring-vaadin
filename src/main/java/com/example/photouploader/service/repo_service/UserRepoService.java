package com.example.photouploader.service.repo_service;

public interface UserRepoService {
    Long getUserIdByUsername(String name);

    boolean isUserIsUsed(String username);

    void saveUser(String username, String password);
}
