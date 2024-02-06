package com.crescendo.playList.service;

import com.crescendo.allPlayList.dto.response.AllPlayResponseDTO;
import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.playList.dto.requestDTO.PlayListRequestDTO;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
import com.crescendo.playList.dto.responseDTO.PlayListofListResponseDTO;
import com.crescendo.playList.entity.PlayList;
import com.crescendo.playList.repository.PlayListRepository;
import com.crescendo.score.entity.Score;
import com.crescendo.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.PackedColorModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class PlayListService {

    private final MemberRepository memberRepository;
    private final AllPlayListRepository allPlayListRepository;
    private final PlayListRepository playListRepository;
    private final ScoreRepository scoreRepository;

    //playList에 나의 악보들을 등록
    public boolean myPlayList(final PlayListRequestDTO dto,String account) {
        try {
            //내 계정을 찾아야 한다 .
            Member member = memberRepository.getOne(account);
            if(member == null){
                System.out.println("계정이 없습니다.");
            }
            //일단 마음에 드는 score를 가져와야 한다.
            Optional<Score> score = scoreRepository.findById(dto.getScoreNo());
            //그리고 나의 (AllPlayList)재생목록들을 가져와야 한다.
            List<AllPlayList> myPlayLists = allPlayListRepository
                    .findByAccount_AccountAndPlId(
                            account,
                            dto.getPlId());
            if((!account.equals(member.getAccount()))){
                System.out.println("본인의 재생목록이 아닙니다.");
                return false;
            }

            //만약 나의 재생목록이 없다면 재생목록을 만든다.
            if (myPlayLists.isEmpty()) {
                //나의 새로운 재생 목록을 생성한다.
                Member member1 = memberRepository.getOne(account);
                AllPlayList newPlayList = AllPlayList.builder()
                        .plName("New PlayList Title")
                        .account(member1)
                        .plShare(false) //새로운 재생 목록은 기본적으로 공유하지 않도록 설정을 해둔다.
                        .build();

                //나의 새로운 저장목록을 만든다.
                allPlayListRepository.save(newPlayList);
                //재생목록이 생겼으면 나의 playlist의 새로운 score(악보)들을 담는다.
                PlayList build = PlayList.builder()
                        .plId(newPlayList)
                        .score(score.get())
                        .build();



                //나의 새로운 playList를 만든다
                playListRepository.save(build);
                PlayListResponseDTO.builder().scoreCount(+1).build();

                log.info("새로운 저장소와 악보를 추가했습니다. ");
            } else {
                PlayList playListByPlIdAndScoreNo = playListRepository.findByPlIdAndScore(dto.getPlId(), dto.getScoreNo());
                if(playListByPlIdAndScoreNo != null){
                    log.info("해당 악보는 본인 재생목록에 존재합니다.");
                    return false;
                }
                //만약 저장소가 있다면 ?
                AllPlayList selectedPlayList = myPlayLists.get(0);

                // 선택한 재생목록에 악보를 추가한다.
                PlayList build = PlayList.builder()
                        .plId(selectedPlayList)
                        .score(score.get())
                        .build();
                playListRepository.save(build);

            }

        } catch (Exception e) {
            System.out.println("선택하신 악보는 없는 악보 입니다..");
        }
        return true;
    }

    // 나의 playList 의 리스트들 만들기
    public List<PlayListResponseDTO> getMyPlayLists(String account, Long plId) {
        List<PlayListResponseDTO> list = new ArrayList<>();
        try {
            // 현재 사용자와 특정 플레이리스트 ID에 해당하는 AllPlayList 가져오기
            List<AllPlayList> byAccountAccount = allPlayListRepository.findByAccount_Account(account);
            //만약 재생목록을 사용하는 사용자가 없으면 내보내기
            if(byAccountAccount == null){
                throw new RuntimeException("이 계정의 재생목록PlayList가 없습니다!");
            }
            //재생목록 사용자가 있으면 for문을 통해 allPlayList에서 PlayList들을 찾기
            byAccountAccount.forEach(allPlayList -> {
                List<PlayList> byPlId = playListRepository.findByPlId(allPlayList);
                if(byPlId == null){
                    System.out.println("플레이 리스트가 없습니다!");
                }
                //score악보들을 담을 새로운 리스트를 생성
                List<Score> scores = new ArrayList<>();
                //playList에서 나의 악보들을 List에 담기
                byPlId.forEach(playList -> {
                    Optional<Score> score = scoreRepository.findById(playList.getScore().getScoreNo());
                    if(score == null){
                        System.out.println("악보들을 찾으실 수 없습니다.");
                    }
                    scores.add(score.get());
                });
                //이제 내가 찾은 악보들을 리스트에 저장해서 반환처리 한다.
                PlayListResponseDTO build = PlayListResponseDTO.builder()
                        .scoreNo(scores)
                        .plId(plId)
                        .build();
                list.add(build);
            });
            return list;
        }catch (Exception e){
            return null;
        }
    }

    // 나의 playList 안에 score를 삭제
    public boolean deleteMyPlayList(String account, Long plId, Long plNo) {
        try {
            // allPlayList의 멤버와 allPlayList Id를 찾는다
            List<AllPlayList> allPlayListByAccountAndPlId = allPlayListRepository.findByAccount_AccountAndPlId(account, plId);
            if (allPlayListByAccountAndPlId == null || allPlayListByAccountAndPlId.isEmpty()) {
                System.out.println("재생목록을 찾을 수 없습니다.");
                return false; // 재생목록을 찾을 수 없으면 종료
            }
            AllPlayList allPlayList = allPlayListByAccountAndPlId.get(0);
            // 특정 plNo와 일치하는 플레이리스트 찾기
            List<PlayList> playlistsToDelete = playListRepository.findByPlId(allPlayList).stream()
                    .filter(playList -> playList.getPlNo().equals(plNo))
                    .collect(Collectors.toList());

            // 찾은 플레이리스트들 삭제
            playlistsToDelete.forEach(playListRepository::delete);

            return true;
        } catch (Exception e) {
            System.out.println("allPlayList를 찾으실 수 없습니다.");
            return false;
        }
    }



}
