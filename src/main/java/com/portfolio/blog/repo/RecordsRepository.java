package com.portfolio.blog.repo;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecordsRepository extends JpaRepository<Records, Long> {

    @Query("select r from Records r where r.member = ?1")
    List<Records> findAllByMember(Member member);


    Optional<Records> findAllByRecordId(String recordId);
}
