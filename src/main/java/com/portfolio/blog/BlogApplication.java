package com.portfolio.blog;

import com.portfolio.blog.domain.activity.Type;
import com.portfolio.blog.domain.post.Category;
import com.portfolio.blog.service.ActivityService;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.activity.ActivityRequest;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.post.PostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

@Slf4j
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

    @Autowired
    private ActivityService activityService;


 // 프론트 꾸밀때 쓸거
    @PostConstruct
    public void test() {
        System.out.println();
        MemberCreate memberCreate = new MemberCreate();
        memberCreate.setEmail("myEmail@embeddedhello.com");
        memberCreate.setName("usernameZIOGENOR");
        memberCreate.setRealName("PIO");
        memberCreate.setPassword("1!");

        String memberId = memberService.save(memberCreate);
        memberService.testVerified(memberId);

        MemberCreate memberCreate1 = new MemberCreate();
        memberCreate.setEmail("leeseoho@korea.com");
        memberCreate.setName("usernameLSEK");
        memberCreate.setRealName("UIO");
        memberCreate.setPassword("1!");

        String memberId1 = memberService.save(memberCreate);
        memberService.testVerified(memberId1);


        PostRequest postRequest = new PostRequest();
        postRequest.setCategory(Category.STUDY.name());
        postRequest.setTitle("title");
        postRequest.setContents("내용");

        postService.save(postRequest, memberId);

        PostRequest postRequest1 = new PostRequest();
        postRequest1.setCategory(Category.STUDY.name());
        postRequest1.setTitle("titlasdasde");
        postRequest1.setContents("asqvasr내aseasasd용");

        postService.save(postRequest1, memberId);

        PostRequest postRequest2 = new PostRequest();
        postRequest2.setCategory(Category.STUDY.name());
        postRequest2.setTitle("titascase");
        postRequest2.setContents("내qwtvsdgaetEWFC용");

        postService.save(postRequest2, memberId);

        PostRequest postRequest3 = new PostRequest();
        postRequest3.setCategory(Category.STUDY.name());
        postRequest3.setTitle("titASDCFADCFASDFAEWle");
        postRequest3.setContents("내FASDCFASDRAERACE용");

        postService.save(postRequest3, memberId);

        PostRequest postRequest4 = new PostRequest();
        postRequest4.setCategory(Category.WORK.name());
        postRequest4.setTitle("작업물");
        postRequest4.setContents("내용내뇽");

        postService.save(postRequest4, memberId);

        ActivityRequest activityRequest = new ActivityRequest();
        activityRequest.setName("Java");
        activityRequest.setType("KNOWLEDGE");
        activityService.save(activityRequest, memberId);


        ActivityRequest activityRequest1 = new ActivityRequest();
        activityRequest1.setName("Spring");
        activityRequest1.setType(Type.KNOWLEDGE.name());
        activityService.save(activityRequest1, memberId);

        ActivityRequest activityRequest2 = new ActivityRequest();
        activityRequest2.setName("Jpa");
        activityRequest2.setType(Type.KNOWLEDGE.name());
        activityService.save(activityRequest2, memberId);

        ActivityRequest activityRequest3 = new ActivityRequest();
        activityRequest3.setType(Type.CAREER.name());
        activityRequest3.setName("Naver");
        activityRequest3.setStartPeriod(setDate(2021,4,3));
        activityRequest3.setEndPeriod(setDate(2022,8,2));
        activityRequest3.setStory("I worked at Naver.");
        activityService.save(activityRequest3, memberId);

        ActivityRequest ac = new ActivityRequest();
        ac.setType(Type.CAREER.name());
        ac.setName("Toss");
        ac.setStartPeriod(setDate(2019,4,4));
        ac.setEndPeriod(setDate(2021,4,1));
        ac.setStory("I worked at Toss. it's very nice company!!");
        activityService.save(ac, memberId);


        ActivityRequest activityRequest4 = new ActivityRequest();
        activityRequest4.setType(Type.EDUCATION.name());
        activityRequest4.setName("university");
        activityRequest4.setStartPeriod(setDate(2017,3,1));
        activityRequest4.setEndPeriod(setDate(2021,7,2));
        activityRequest4.setStory("I studied at ~University.");
        activityService.save(activityRequest4,memberId);

    }

    private static Date setDate(int y,int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1, d);
        return new Date(cal.getTimeInMillis());
    }

}
