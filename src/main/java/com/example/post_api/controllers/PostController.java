package com.example.post_api.controllers;

import com.example.post_api.model.Post;
import com.example.post_api.services.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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



}
