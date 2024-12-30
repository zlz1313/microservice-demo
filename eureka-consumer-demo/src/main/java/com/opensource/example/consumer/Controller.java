package com.opensource.example.consumer;

import com.opensource.example.feign.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/consumer")
public class Controller {

    @Autowired
    private Provider provider;

    @GetMapping("/test")
    public String get() {
        return "The msg received from provider is " + provider.doFeign();
    }
}
