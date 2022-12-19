package com.portfolio.blog;

import com.portfolio.blog.domain.Category;
import com.portfolio.blog.domain.PostSearch;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.post.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;

    @PostConstruct
    public void test() {
        MemberCreate memberCreate = new MemberCreate();
        memberCreate.setEmail("lsek");
        memberCreate.setName("sel");
        memberCreate.setPassword("1!");

        String memberId = memberService.save(memberCreate);


        PostCreate postCreate = new PostCreate();
        postCreate.setCategory(Category.STUDY);
        postCreate.setTitle("title");
        postCreate.setSubTitle("subTitle");
        postCreate.setContents("내용");

        postService.save(postCreate, memberId);

        PostCreate postCreate1 = new PostCreate();
        postCreate1.setCategory(Category.STUDY);
        postCreate1.setTitle("titlasdasde");
        postCreate1.setSubTitle("subTxasrqscitle");
        postCreate1.setContents("asqvasr내aseasasd용");

        postService.save(postCreate1, memberId);

        PostCreate postCreate2 = new PostCreate();
        postCreate2.setCategory(Category.STUDY);
        postCreate2.setTitle("titascase");
        postCreate2.setSubTitle("suqwwcacbTitle");
        postCreate2.setContents("내qwtvsdgaetEWFC용");

        postService.save(postCreate2, memberId);

        PostCreate postCreate3 = new PostCreate();
        postCreate3.setCategory(Category.STUDY);
        postCreate3.setTitle("titASDCFADCFASDFAEWle");
        postCreate3.setSubTitle("subTitCAEFCACFASDFACSle");
        postCreate3.setContents("내FASDCFASDRAERACE용");

        postService.save(postCreate3, memberId);
    }

}
