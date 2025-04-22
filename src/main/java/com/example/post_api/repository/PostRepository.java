package com.example.post_api.repository;

import com.example.post_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(String author);
}
