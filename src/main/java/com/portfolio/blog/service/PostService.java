package com.portfolio.blog.service;

import com.portfolio.blog.domain.post.Category;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.post.Post;
import com.portfolio.blog.domain.post.PostSearch;
import com.portfolio.blog.dto.PostDto;
import com.portfolio.blog.exception.CategoryNotMatchingException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.PostNotFoundException;
import com.portfolio.blog.exception.UnAuthenticationAccessException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.repo.PostRepository;
import com.portfolio.blog.vo.post.PostRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(PostRequest postRequest, String memberId) {
        this.validatedCategory(postRequest.getCategory());
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        Post post = Post.create(postRequest, member);
        postRepository.save(post);
        return post.getId();
    }


    @Transactional(readOnly = true)
    public PostDto findOne(String postId) {
        Post post = postRepository.findPostByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException("post not in database"));
        return new ModelMapper().map(post, PostDto.class);
    }


    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> new ModelMapper().map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> findAllByMemberId(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        List<Post> posts = postRepository.findPostByMember(member);
        return posts.stream().map(post -> new ModelMapper().map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> findByMemberIdAndCategory(String memberId,String category) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        this.validatedCategory(category);
        Category categoryEnum = Category.from(category).get();
        List<Post> posts = postRepository.findPostByMemberAndCategory(member, categoryEnum);
        return posts.stream().map(post -> new ModelMapper().map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public PostDto update(PostRequest postUpdate, PostSearch postSearch) {
        this.validatedCategory(postUpdate.getCategory());
        Post post = this.checkOwnPost(postSearch);
        post.update(postUpdate);
        return new ModelMapper().map(post, PostDto.class);
    }

    @Transactional
    public void delete(PostSearch postSearch) {
        Post post = this.checkOwnPost(postSearch);
        postRepository.delete(post);
    }

    private Post checkOwnPost(PostSearch postSearch) {
        Post post = postRepository.findPostByPostId(postSearch.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post not in database"));
        if (post.getMember().getMemberId().equals(postSearch.getMemberId())) {
            throw new UnAuthenticationAccessException("this post is not your");
        }
        return post;
    }


    private void validatedCategory(String category) {
        Category.from(category).orElseThrow(() -> new CategoryNotMatchingException("category is not matched"));
    }

}
