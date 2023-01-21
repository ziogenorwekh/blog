package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.activity.Activity;
import com.portfolio.blog.domain.activity.Type;
import com.portfolio.blog.dto.ActivityDto;
import com.portfolio.blog.exception.ActivityNotFoundException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.TypeNotMatchingException;
import com.portfolio.blog.exception.UnAuthenticationAccessException;
import com.portfolio.blog.repo.ActivityRepository;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.vo.activity.ActivityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public String save(ActivityRequest activityRequest, String memberId) {
        Member member = this.checkMember(memberId);
        Activity activity = Activity.create(activityRequest, member);
        activityRepository.save(activity);
        return activity.getActivityId();
    }

    @Transactional(readOnly = true)
    public ActivityDto findOne(String activityId) {
        Activity activity = activityRepository.findByActivityId(activityId)
                .orElseThrow(() -> new ActivityNotFoundException("activity not in database"));
        return new ModelMapper().map(activity, ActivityDto.class);
    }

    @Transactional(readOnly = true)
    public List<ActivityDto> findAll(String memberId) {
        Member member = checkMember(memberId);
        List<Activity> activities = activityRepository.findAllByMember(member);
        return activities.stream().map(activity -> new ModelMapper().map(activity, ActivityDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityDto> findAllByType(String type, String memberId) {
        Optional<Type> from = Type.from(type);
        Member member = checkMember(memberId);
        Type enumType = from.orElseThrow(() -> new TypeNotMatchingException("type is not matched."));
        List<Activity> activities = activityRepository.findAllByMemberAndActivityType(member, enumType);
        return activities.stream().map(activity -> new ModelMapper().map(activity, ActivityDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ActivityDto update(String memberId, String activityId,ActivityRequest request) {
        Member member = checkMember(memberId);
        Activity activity = activityRepository.findByActivityId(activityId)
                .orElseThrow(() -> new ActivityNotFoundException("activity not in database"));
        if (!activity.getMember().equals(member)) {
            throw new UnAuthenticationAccessException("you're not access.");
        }

        activity.update(request);
        return new ModelMapper().map(activity, ActivityDto.class);
    }

    @Transactional
    public void delete(String memberId, String activityId) {
        Member member = checkMember(memberId);
        Activity activity = activityRepository.findByActivityId(activityId)
                .orElseThrow(() -> new ActivityNotFoundException("activity not in database"));
        if (!activity.getMember().equals(member)) {
            throw new UnAuthenticationAccessException("you're not access.");
        }
        activity.delete();
        activityRepository.delete(activity);
    }

    private Member checkMember(String memberId) {
        return memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
    }
}
