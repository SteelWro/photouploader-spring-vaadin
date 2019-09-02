package com.example.photouploader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestApi {

    @GetMapping("/admin")
    public String test1() {
        return "ADMIN site";
    }

    @GetMapping("/user")
    public String test2() {
        return "USER site";
    }
}
