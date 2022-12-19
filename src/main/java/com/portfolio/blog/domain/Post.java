package com.portfolio.blog.domain;

import com.portfolio.blog.vo.post.PostCreate;
import com.portfolio.blog.vo.post.PostUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID", nullable = false)
    private Long id;

    @Column(name = "POST_UUID", unique = true)
    private String postId;

    private String title;

    private String subTitle;

    private String contents;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedBy;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Post(String title, String subTitle, String contents,Category category) {
        this.postId = UUID.randomUUID().toString();
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.category = category;
    }

    // Business Logic
    public static Post create(PostCreate postCreate, Member member) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .subTitle(postCreate.getSubTitle())
                .contents(postCreate.getContents())
                .category(postCreate.getCategory())
                .build();
        post.addMember(member);
        return post;
    }

    public void addMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }


    public void update(PostUpdate postUpdate) {
        this.title = postUpdate.getTitle();
        this.subTitle = postUpdate.getSubTitle();
        this.contents = postUpdate.getContents();
    }

    public void delete() {
        this.member = null;
    }
}
