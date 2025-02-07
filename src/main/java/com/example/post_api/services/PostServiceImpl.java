package com.example.post_api.services;

import com.example.post_api.model.Post;
import com.example.post_api.repository.PostRepository;
import com.example.post_api.services.feign.ImageLoaderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final ImageLoaderApi imageLoaderApi;


    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }
}
