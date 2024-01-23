package com.crescendo.inquiry.service;

import com.crescendo.inquiry.dto.request.InquirySaveRequestDTO;
import com.crescendo.inquiry.dto.response.FoundInquiryListResponseDTO;
import com.crescendo.inquiry.entity.Inquiry;
import com.crescendo.member.entity.Member;
import com.crescendo.inquiry.repository.InquiryRepository;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    //문의 추가하기
    public boolean save(InquirySaveRequestDTO dto){
        Member foundMember = memberRepository.getOne(dto.getAccount());
        if (foundMember == null){
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        Inquiry inquiry = Inquiry.builder().inquiryTitle(dto.getTitle()).inquiryContent(dto.getContent()).member(foundMember).build();
        inquiryRepository.save(inquiry);
        return true;
    }

    // 계정명으로 문의 전체 조회하기
    public List<FoundInquiryListResponseDTO> findAllByAccount(String account){
        if(!memberRepository.existsByAccount(account)){
            throw new NoMatchAccountException("정확한 계정명을 주세요");
        }
        List<Inquiry> foundList = inquiryRepository.findAllByMemberAccountOrderByInquiryDateTimeDesc(account);
        List<FoundInquiryListResponseDTO> foundListPackage = new ArrayList<>();
        foundList.forEach(inquiry -> {
            foundListPackage.add(new FoundInquiryListResponseDTO(inquiry));
        });
        return foundListPackage;
    }


    // 문의 아이디로 문의 삭제 하기
    public boolean deleteById(String inquiryId){
        if(!inquiryRepository.existsById(inquiryId)){
            throw new NoMatchAccountException("정확한 아이디를 주세요"); // <- 만들기 귀찮아서 나중에 만들어라
        }
        inquiryRepository.deleteById(inquiryId);
        return true;
    }
}
