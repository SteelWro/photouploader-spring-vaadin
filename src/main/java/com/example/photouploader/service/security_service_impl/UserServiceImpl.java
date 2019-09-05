package com.example.photouploader.service.security_service_impl;

import com.example.photouploader.model.User;
import com.example.photouploader.model.Role;
import com.example.photouploader.repo.UserRepo;
import com.example.photouploader.service.security_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Long getUserIdByUsername(String name) {
        User user = userRepo.findByUsername(name);
        return user.getId();
    }

    @Override
    public boolean isUserIsUsed(String username) {
        return(userRepo.findByUsername(username) != null);
    }

    @Override
    public void saveUser(String username, String password) {
        userRepo.save(new User(username, password, Role.USER));
    }
}
