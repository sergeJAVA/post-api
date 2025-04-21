package com.example.post_api.services;

import com.example.post_api.model.Image;
import com.example.post_api.model.Post;
import com.example.post_api.repository.PostRepository;
import com.example.post_api.services.feign.ImageLoaderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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

    @Override
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post createPost(MultipartFile file, Long userId, Post post, String author) {
        Post newPost = Post.builder()
                .title(post.getTitle())
                .author(author)
                .description("")
                .imagePath("")
                .build();
        Post savedPost = postRepository.save(newPost);
        Image image = imageLoaderApi.uploadImage(file, userId.toString(), post.getTitle(), savedPost.getId().toString());
        savedPost.setTitle(post.getTitle());
        savedPost.setDescription(post.getDescription());
        savedPost.setImagePath(image.getDownloadPath());

        return postRepository.save(savedPost);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }


}
