package com.portfolio.blog.domain;

import com.portfolio.blog.vo.post.PostCreate;
import com.portfolio.blog.vo.post.PostUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


//    @OneToMany(mappedBy = "post",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<UploadFile> files = new ArrayList<>();

    @Builder
    public Post(String title, String contents,Category category) {
        this.postId = UUID.randomUUID().toString();
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    // Business Logic
    public static Post create(PostCreate postCreate, Member member) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .contents(postCreate.getContents())
                .category(Category.from(postCreate.getCategory()).get())
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
        this.contents = postUpdate.getContents();
        this.category = Category.from(postUpdate.getCategory()).get();
    }

    public void delete() {
        this.member = null;
    }
}
