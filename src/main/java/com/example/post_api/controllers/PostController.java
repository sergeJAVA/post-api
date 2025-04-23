package com.example.post_api.controllers;

import com.example.post_api.model.Post;
import com.example.post_api.services.PostService;
import com.example.post_api.services.feign.CommentsApi;
import com.example.post_api.services.feign.ImageLoaderApi;
import com.example.post_api.services.feign.LikeApi;
import com.example.post_api.services.security.JWTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ImageLoaderApi imageLoaderApi;
    private final CommentsApi commentsApi;
    private final LikeApi likeApi;
    private final JWTService jwtService;

    @GetMapping("/all-posts")
    public List<Post> posts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPostById(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (Optional.ofNullable(post).isPresent()) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public Post createPost(@RequestParam("file") MultipartFile file,
                           @CookieValue("token")String token,
                           @RequestParam("post") String postJson)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = objectMapper.readValue(postJson, Post.class);
        return postService.createPost(file, jwtService.getUserIdFromToken(token), post, jwtService.getUserNameFromToken(token));
    }

    @PostMapping("/update/author/{oldAuthor}/{newAuthor}")
    public ResponseEntity<String> updateAuthors(@PathVariable String oldAuthor,@PathVariable String newAuthor) {
        List<Post> posts = postService.updateAuthor(oldAuthor, newAuthor);
        if (posts != null) {
            return ResponseEntity.ok("The author has been successfully changed");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The author has not been changed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post != null) {
            postService.deletePostById(id);
            commentsApi.deleteCommentsByPostId(id);
            likeApi.removeAllLikesFromPost(id);
            imageLoaderApi.deleteImageWithPost(id.toString());

            return ResponseEntity.ok("The post with id \""+ id + "\" has been deleted");
        }
        return new ResponseEntity<>("The post with id \"" + id + "\" was not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/image")
    public ResponseEntity<String> deleteImageFromPost(@RequestParam String key) {
        imageLoaderApi.deleteImage(key);
        return ResponseEntity.ok("The image has been deleted");
    }
}
