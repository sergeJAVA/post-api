package com.example.post_api.services.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "like-api", url = "${feign.like-api-service.url}")
public interface LikeApi {
    @DeleteMapping("/{postId}/all")
    void removeAllLikesFromPost(@PathVariable Long postId);
}
