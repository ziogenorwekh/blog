package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Post;
import com.portfolio.blog.dto.PostDto;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.PostNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.repo.PostRepository;
import com.portfolio.blog.vo.PostCreate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(PostCreate postCreate, String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        Post post = Post.create(postCreate, member);
        postRepository.save(post);
        return post.getId();
    }


    @Transactional(readOnly = true)
    public PostDto findOne(String postId) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow(() -> new PostNotFoundException("post not in database"));
        return new ModelMapper().map(post, PostDto.class);
    }
}
