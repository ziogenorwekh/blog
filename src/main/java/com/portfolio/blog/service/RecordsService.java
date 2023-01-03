package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Records;
import com.portfolio.blog.dto.RecordsDto;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.RecordNotFountException;
import com.portfolio.blog.exception.UnAuthenticationAccessException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.repo.RecordsRepository;
import com.portfolio.blog.vo.records.RecordCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordsService {
    private final RecordsRepository recordsRepository;
    private final MemberRepository memberRepository;

    public String save(RecordCreate recordCreate, String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        Records records = Records.create(recordCreate, member);
        recordsRepository.save(records);
        return records.getRecordId();
    }

    public List<RecordsDto> findOneByMember(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        List<Records> list = recordsRepository.findAllByMember(member);
        return list.stream().map(records ->
                new RecordsDto(records, member.getName())).collect(Collectors.toList());
    }

    public RecordsDto findOne(String recordsId) {
        Records records = recordsRepository.findAllByRecordId(recordsId)
                .orElseThrow(() -> new RecordNotFountException("records not in database"));
        return new RecordsDto(records, records.getMember().getName());
    }

    public void delete(String recordsId, String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        Records records = recordsRepository.findAllByRecordId(recordsId)
                .orElseThrow(() -> new RecordNotFountException("records not in database"));

        if (!records.getMember().equals(member)) {
            throw new UnAuthenticationAccessException("you're not access this entity");
        }
        records.delete();
        recordsRepository.delete(records);
    }

}
