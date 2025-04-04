package com.example.post_api.services;

import com.example.post_api.model.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();

    Post save(Post post);

    void deletePostById(Long postId);
}
