package com.crescendo.restore.service;

import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.restore.dto.response.RestoreWhetherResponseDTO;
import com.crescendo.restore.entity.Restore;
import com.crescendo.restore.exception.ExistsInRestoreException;
import com.crescendo.restore.repository.RestoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RestoreService {
    private final RestoreRepository restoreRepository;
    private final MemberRepository memberRepository;


    // Restore에 추가하기
    public boolean add(String account){
        boolean flag = restoreRepository.existsByMemberAccount(account);
        // 삭제 요청이 이미 있을 때
        if(flag){
            throw new ExistsInRestoreException("이미 삭제 요청에 있습니다");
        }
        // 계정명이 존재 하지 않을 때
        Member foundMember = memberRepository.getOne(account);
        if(foundMember==null){
            throw new NoMatchAccountException("계정명이 존재 하지 않습니다");
        }

        Restore build = Restore.builder().member(foundMember).build();


        restoreRepository.save(build);
        return true;
    }

    // 계정 삭제 여부 확인
    public RestoreWhetherResponseDTO find(String account){
        boolean flag = restoreRepository.existsByMemberAccount(account);
        if(!flag){
            throw new ExistsInRestoreException("삭제 요청이 없습니다");
        }

        Restore byMemberAccount = restoreRepository.findByMemberAccount(account);
        RestoreWhetherResponseDTO build = RestoreWhetherResponseDTO.builder()
                .restoreNo(byMemberAccount.getRestoreNo())
                .account(byMemberAccount.getMember().getAccount())
                .build();

        return build;

    }



    // 계정 삭제 취소하기
    public boolean cancel(String restoreNo){
        boolean flag = restoreRepository.existsById(restoreNo);
        if(!flag){
            throw new ExistsInRestoreException("삭제 요청이 없습니다");
        }

        restoreRepository.deleteById(restoreNo);
        return true;

    }

}
