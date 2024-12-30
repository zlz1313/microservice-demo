package com.opensource.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "provider", path = "/provider", configuration = FeignConfiguration.class)
public interface Provider {

    @PostMapping(value = "/test", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String doFeign();
}
