package com.example.photouploader;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {

    @GetMapping("/test1")
    public String test1() {
        return "ADMIN";
    }

    @GetMapping("/test2")
    public String test2() {
        return "USER";
    }
}
