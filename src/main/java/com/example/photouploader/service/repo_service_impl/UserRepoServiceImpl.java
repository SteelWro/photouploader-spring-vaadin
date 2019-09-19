package com.example.photouploader.service.repo_service_impl;

import com.example.photouploader.model.User;
import com.example.photouploader.model.Role;
import com.example.photouploader.repo.UserRepo;
import com.example.photouploader.service.repo_service.UserRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRepoServiceImpl implements UserRepoService {
    PasswordEncoder passwordEncoder;
    UserRepo userRepo;

    @Autowired
    public UserRepoServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public Long getUserIdByUsername(String name) {
        User user = userRepo.findByUsername(name);
        return user.getId();
    }

    @Override
    public boolean isUserIsUsed(String username) {
        return (userRepo.findByUsername(username) != null);
    }

    @Override
    public void saveUser(String username, String password) {
        userRepo.save(new User(username, passwordEncoder.encode(password), Role.USER));
    }
}
