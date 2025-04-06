package com.example.post_api.services.feign;

import com.example.post_api.model.Image;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "image-loader-api", url = "${feign.image-loader-service.url}")
public interface ImageLoaderApi {
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Image uploadImage(@RequestBody MultipartFile file, @RequestParam String userId, @RequestParam String title);

    @DeleteMapping("/delete")
    void deleteImage(@RequestParam String key);

    @DeleteMapping("/delete/image")
    void deleteImage(@RequestParam("userId") String userId, @RequestParam("name") String name);

    @GetMapping("/home")
    List<Image> home();


}
