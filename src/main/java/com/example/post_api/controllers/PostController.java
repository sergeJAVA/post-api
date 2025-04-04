package com.example.post_api.controllers;

import com.example.post_api.model.Image;
import com.example.post_api.model.Post;
import com.example.post_api.services.PostService;
import com.example.post_api.services.feign.CommentsApi;
import com.example.post_api.services.feign.ImageLoaderApi;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ImageLoaderApi imageLoaderApi;
    private final CommentsApi commentsApi;

    @GetMapping("/all-posts")
    public List<Post> posts() {
        return postService.findAll();
    }

    @PostConstruct
    public void postLoad() {
        Post kvadrat = Post.builder()
                .title("Чёрный Квадрат Серёги")
                .author("Серёга")
                .description("o4en` krasiviy kvadrat")
                .imagePath("kvadrat_seregi")
                .build();

        postService.save(kvadrat);
    }

    @PostMapping("/create")
    public Image createPost(@RequestBody MultipartFile file, @RequestParam String userId) {
        Image image = imageLoaderApi.uploadImage(file, userId);
        Post post = Post.builder()
                .imagePath(image.getDownloadPath())
                .build();
        postService.save(post);
        return image;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        commentsApi.deleteCommentsByPostId(id);
        return ResponseEntity.ok("the post with id \""+ id + "\" has been deleted");
    }

    @DeleteMapping("/delete/image")
    public ResponseEntity<String> deleteImageFromPost(@RequestParam String key) {
        imageLoaderApi.deleteImage(key);
        return ResponseEntity.ok("The image has been deleted");
    }
}
