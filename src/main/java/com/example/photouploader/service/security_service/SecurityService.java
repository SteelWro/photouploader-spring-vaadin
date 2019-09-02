package com.example.photouploader.service.security_service;

public interface SecurityService {
    String findLoggedInUsername();
    void autoLogin(String username, String password);
}
