package com.example.post_api.services.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "comments-api", url = "${feign.comments-api-service.url}")
public interface CommentsApi {

    @DeleteMapping("/post/{postId}/comments")
    public ResponseEntity<String> deleteCommentsByPostId(@PathVariable Long postId);
}
