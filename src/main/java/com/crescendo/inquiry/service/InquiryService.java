package com.crescendo.inquiry.service;

import com.crescendo.inquiry.dto.request.InquirySaveRequestDTO;
import com.crescendo.entity.Inquiry;
import com.crescendo.member.entity.Member;
import com.crescendo.inquiry.repository.InquiryRepository;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    //문의 추가하기
    public boolean save(InquirySaveRequestDTO dto){
        System.out.println("dto = " + dto);
        Member foundMember = memberRepository.getOne(dto.getAccount());
        System.out.println("foundMember = " + foundMember);
        if(foundMember != null){
            Inquiry inquiry = Inquiry.builder().inquiryTitle(dto.getTitle()).inquiryContent(dto.getContent()).member(foundMember).build();
            inquiryRepository.save(inquiry);
            return true;
        }
        return false;

    }
}
