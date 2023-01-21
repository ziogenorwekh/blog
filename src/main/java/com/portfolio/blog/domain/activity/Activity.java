package com.portfolio.blog.domain.activity;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.exception.TypeNotMatchingException;
import com.portfolio.blog.vo.activity.ActivityRequest;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long id;

    @Column(name = "ACTIVITY_UUID", nullable = false, unique = true)
    private String activityId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Lob
    private String story;

    private Date period;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Type activityType;


    @Builder
    public Activity(String name, String story, Date period, Type activityType) {
        this.activityId = UUID.randomUUID().toString();
        this.name = name;
        this.story = story;
        this.period = period;
        this.activityType = activityType;
    }

    public void addMember(Member member) {
        this.member = member;
        this.getMember().getActivities().add(this);
    }

    public static Activity create(ActivityRequest activityRequest, Member member) {
        Optional<Type> typeOptional = Type.from(activityRequest.getType());
        Type type = typeOptional.orElseThrow(() -> new TypeNotMatchingException("type is not matched."));
        Activity activity = Activity.builder().name(activityRequest.getName())
                .story(activityRequest.getStory())
                .period(activityRequest.getPeriod())
                .activityType(type).build();
        activity.addMember(member);
        return activity;
    }

    public void update(ActivityRequest activityRequest) {
        Optional<Type> typeOptional = Type.from(activityRequest.getType());
        Type type = typeOptional.orElseThrow(() -> new TypeNotMatchingException("type is not matched."));
        this.name = activityRequest.getName();
        this.story = activityRequest.getStory();
        this.period = activityRequest.getPeriod();
        this.activityType = type;
    }

    public void delete() {
        this.member = null;
    }

}
