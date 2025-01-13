package com.example.post_api.services.feign;

import com.example.post_api.model.Image;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "image-loader-api", url = "feign.image-loader-service.url")
public interface ImageLoaderApi {
    @PostMapping("/upload")
    Image uploadImage(@RequestBody MultipartFile file, @RequestParam String userId);
}
