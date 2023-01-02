package com.portfolio.blog;

import com.portfolio.blog.domain.Category;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.PostSearch;
import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.post.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SpringBootApplication
public class BlogApplication {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
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
        memberService.testVerified(memberId);

        PostCreate postCreate = new PostCreate();
        postCreate.setCategory(Category.STUDY.name());
        postCreate.setTitle("title");
        postCreate.setContents("내용");

        postService.save(postCreate, memberId);

        PostCreate postCreate1 = new PostCreate();
        postCreate1.setCategory(Category.STUDY.name());
        postCreate1.setTitle("titlasdasde");
        postCreate1.setContents("asqvasr내aseasasd용");

        postService.save(postCreate1, memberId);

        PostCreate postCreate2 = new PostCreate();
        postCreate2.setCategory(Category.STUDY.name());
        postCreate2.setTitle("titascase");
        postCreate2.setContents("내qwtvsdgaetEWFC용");

        postService.save(postCreate2, memberId);

        PostCreate postCreate3 = new PostCreate();
        postCreate3.setCategory(Category.STUDY.name());
        postCreate3.setTitle("titASDCFADCFASDFAEWle");
        postCreate3.setContents("내FASDCFASDRAERACE용");

        postService.save(postCreate3, memberId);

        PostCreate postCreate4 = new PostCreate();
        postCreate4.setCategory(Category.WORK.name());
        postCreate4.setTitle("작업물");
        postCreate4.setContents("내용내뇽");

        postService.save(postCreate4, memberId);
    }

}
