package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostByPostId(String postId);
}
