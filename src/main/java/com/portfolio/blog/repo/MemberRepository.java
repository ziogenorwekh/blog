package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByMemberId(String memberId);
}
