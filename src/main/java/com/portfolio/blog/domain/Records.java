package com.portfolio.blog.domain;

import com.portfolio.blog.vo.records.RecordCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_UUID", unique = true)
    private String recordId;

    // 상장 사진 등
    private String fileUrl;

    @Column(nullable = false)
    private String awardsTitle;

    @Lob
    private String history;

    // 수상 일자
    private Date awardsDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @Builder
    public Records(String awardsTitle, String history, Date awardsDate, String recordId,String fileUrl) {
        this.awardsTitle = awardsTitle;
        this.recordId = recordId;
        this.history = history;
        this.fileUrl = fileUrl;
        this.awardsDate = awardsDate;
    }

    public static Records create(RecordCreate recordCreate, Member member) {
        String uuid = UUID.randomUUID().toString();
        Records records = Records.builder()
                .recordId(uuid)
                .awardsDate(recordCreate.getAwardsDate())
                .history(recordCreate.getHistory())
                .awardsTitle(recordCreate.getAwardsTitle())
                .fileUrl(recordCreate.getFileUrl())
                .build();
        records.addMember(member);
        return records;
    }

    public void addMember(Member member) {
        this.member = member;
        member.getRecords().add(this);
    }

    public void delete() {
        this.member = null;
        member.getRecords().remove(this);
    }
}
