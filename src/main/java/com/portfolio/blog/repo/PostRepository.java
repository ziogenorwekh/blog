package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Category;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostByPostId(String postId);

    List<Post> findPostByMember(Member member);

    @Query("select p from Post p where p.member = ?1 and p.category = ?2")
    List<Post> findPostByMemberAndCategory(Member member, Category category);
}
