package com.crescendo.allPlayList.service;

import com.crescendo.allPlayList.dto.request.AllPlayListRequestDTO;
import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.allPlayList.dto.response.AllPlayListResponseDTO;
import com.crescendo.allPlayList.dto.response.AllPlayResponseDTO;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // !! JPA 사용시 필수!!
public class AllPlayListService {

    private final AllPlayListRepository allPlayListRepository;
    private final MemberRepository memberRepository;

    // AllPlayList에 나의 playList 등록 !
    public AllPlayResponseDTO create(AllPlayListRequestDTO dto,String account ) {
        Member member = memberRepository.getOne(account);
        if (member == null) {
            return null;
        }

        AllPlayList build = AllPlayList.builder().plName(dto.getPlName()).account(member).build();
        allPlayListRepository.save(build);
//        log.info("새로운 PlayList를 내 마음속에 저★장★ : {}", dto.getPlName());

        return retrieve(account);
    }



    //AllPlayList 불러오기 !
    public AllPlayResponseDTO retrieve(String account){
        List<AllPlayList> allPlayList= allPlayListRepository.findByAccount_Account(account);
        List<AllPlayListResponseDTO> collect = allPlayList.stream()
                .map(AllPlayListResponseDTO::new)
                .collect(Collectors.toList());
        return AllPlayResponseDTO.builder().allPlayLists(collect).build();
    }


    //AllPlayList 수정 하기
    public boolean modifyAllPlayList(AllPlayListRequestDTO dto,String account) {
//        log.info("수정 하기 service : {}", dto);
        AllPlayList findId = allPlayListRepository.getOne(dto.getPlId());
        if (!findId.getAccount().getAccount().equals(account)) {
            return false;
        }
        findId.setPlName(dto.getPlName());
        return true;
    }

    //AllPlayList 삭제 !
    public AllPlayResponseDTO delete(Long plId, String account) {
        try {
            List<AllPlayList> allPlayList = allPlayListRepository.findByAccount_AccountAndPlId(account, plId);
            if(allPlayList == null || allPlayList.isEmpty()){
                System.out.println("계정 또는 재생목록이 없습니다.");
            }
            allPlayListRepository.deleteById(plId);
        } catch (Exception e) {
            log.error("재생목록의 번호가 존재 하지 않아요 !  - ID {}, error - {}", plId, e.getMessage());
            throw new RuntimeException("삭제실패 했습니다.");
        }
        return retrieve(account);
    }

    private void MinusScoreCount(AllPlayList allPlayList){
        allPlayList.setScoreCount(allPlayList.getScoreCount() -1);
        allPlayListRepository.save(allPlayList);
        log.info("재생목록 '{}'의 scoreCount가 1 감소 하였습니다.", allPlayList.getPlName());
    }


}
