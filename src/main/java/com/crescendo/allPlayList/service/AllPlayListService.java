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
        //재생목록(AllPlayList)가 3개 이상이면 더 이상 못만들도록 처리
        int allPlayList= allPlayListRepository.countByAccount(account);

        int maxAllPlayListCounts = 3;

        if(allPlayList >= maxAllPlayListCounts){
            log.warn("더 이상 재생목록을 만드실 수 없습니다.");
            return null;
        }

        AllPlayList build = AllPlayList.builder().plName(dto.getPlName()).account(member).plShare(dto.isPlShare()).build();
        allPlayListRepository.save(build);
        log.info("새로운 PlayList를 내 마음속에 저★장★ : {}", dto.getPlName());

//        PlusScoreCount(build);
        return retrieve();
    }

    //allPlayList안에 playList가 증가하면 scoreCount가 +1 추가 되는 로직
    private void PlusScoreCount(AllPlayList allPlayList) {
        allPlayList.setScoreCount(allPlayList.getScoreCount() + 1);
        allPlayListRepository.save(allPlayList);
        log.info("재생목록 '{}'의 scoreCount가 1 증가하였습니다.", allPlayList.getPlName());
    }

    //AllPlayList 불러오기 !
    public AllPlayResponseDTO retrieve() {
        List<AllPlayList> findAll = allPlayListRepository.findAll();
        List<AllPlayListResponseDTO> collect = findAll.stream()
                .map(AllPlayListResponseDTO::new)
                .collect(Collectors.toList());

        return AllPlayResponseDTO.builder().allPlayLists(collect).build();
    }


    //AllPlayList 수정 하기
    public boolean modifyAllPlayList(AllPlayListRequestDTO dto,String account) {
        log.info("수정 하기 service : {}", dto);
        AllPlayList findId = allPlayListRepository.getOne(dto.getPlId());
        if (!findId.getAccount().getAccount().equals(account)) {
            return false;
        }
        findId.setPlName(dto.getPlName());
        findId.setPlShare(dto.isPlShare());
        return true;
    }

    //AllPlayList 삭제 !
    public AllPlayResponseDTO delete(Long plid, String account) {
        try {
            List<AllPlayList> allPlayList = allPlayListRepository.findByAccount_AccountAndPlId(account, plid);
            if(allPlayList == null || allPlayList.isEmpty()){
                System.out.println("계정 또는 재생목록이 없습니다.");
            }
            allPlayListRepository.deleteById(plid);
            MinusScoreCount((AllPlayList) allPlayList);
        } catch (Exception e) {
            log.error("재생목록의 번호가 존재 하지 않아요 !  - ID {}, error - {}", plid, e.getMessage());
            throw new RuntimeException("삭제실패 했습니다.");
        }
        return retrieve();
    }

    private void MinusScoreCount(AllPlayList allPlayList){
        allPlayList.setScoreCount(allPlayList.getScoreCount() -1);
        allPlayListRepository.save(allPlayList);
        log.info("재생목록 '{}'의 scoreCount가 1 감소 하였습니다.", allPlayList.getPlName());
    }
}
