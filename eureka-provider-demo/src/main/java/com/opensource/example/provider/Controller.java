package com.opensource.example.provider;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/provider")
public class Controller {

    @PostMapping("/test")
    public String get() {
        return "Provider No.1";
    }
}
