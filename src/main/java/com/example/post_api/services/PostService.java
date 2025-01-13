package com.example.post_api.services;

import com.example.post_api.model.Image;
import com.example.post_api.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll();

    Post save(Post post);
}
