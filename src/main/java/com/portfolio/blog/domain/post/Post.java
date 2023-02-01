package com.portfolio.blog.domain.post;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.vo.post.PostRequest;
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

    @Lob
    private String contents;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedBy;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Post(String title, String contents,Category category) {
        this.postId = UUID.randomUUID().toString();
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    // Business Logic
    public static Post create(PostRequest postRequest, Member member) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .category(Category.from(postRequest.getCategory()).get())
                .build();
        post.addMember(member);
        return post;
    }

    public void addMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }


    public void update(PostRequest postUpdate) {
        this.title = postUpdate.getTitle();
        this.contents = postUpdate.getContents();
        this.category = Category.from(postUpdate.getCategory()).get();
    }

    public void delete() {
        this.member = null;
    }
}
