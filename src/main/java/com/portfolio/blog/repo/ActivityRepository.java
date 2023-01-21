package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.activity.Activity;
import com.portfolio.blog.domain.activity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("select a from Activity a where a.activityId = ?1")
    Optional<Activity> findByActivityId(String activityId);

    @Query("select a from Activity a where a.member = ?1")
    List<Activity> findAllByMember(Member member);

    @Query("select a from Activity a where a.member = ?1 and a.activityType = ?2")
    List<Activity> findAllByMemberAndActivityType(Member member, Type type);
}
