package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostByPostId(String postId);

    List<Post> findPostByMember(Member member);
}
