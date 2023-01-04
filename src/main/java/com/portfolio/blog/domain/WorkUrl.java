package com.portfolio.blog.domain;

import com.portfolio.blog.vo.WorkUrlCreate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class WorkUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_URL_ID")
    private Long id;

    @Column(name = "WORK_URL_UUID",unique = true)
    private String workUrlID;

    private String url;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @Builder
    public WorkUrl(String workUrlID, String url) {
        this.workUrlID = workUrlID;
        this.url = url;
    }


    public static WorkUrl create(WorkUrlCreate workUrlCreate,Member member) {
        String uuid = UUID.randomUUID().toString();
        WorkUrl workUrl = WorkUrl.builder()
                .workUrlID(uuid)
                .url(workUrlCreate.getUrl())
                .build();
        workUrl.addMember(member);
        return workUrl;
    }

    public void addMember(Member member) {
        this.member = member;
        member.getWorkUrls().add(this);
    }

    public void delete() {
        this.member = null;
    }
}
