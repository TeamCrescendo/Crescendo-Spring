package com.crescendo.playList.service;

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
    public boolean myPlayList(final PlayListRequestDTO dto) {
        try {
            //일단 마음에 드는 score를 가져와야 한다.

            Optional<Score> score = scoreRepository.findById(dto.getScoreNo());
            //그리고 나의 (AllPlayList)재생목록들을 가져와야 한다.
            List<AllPlayList> myPlayLists = allPlayListRepository
                    .findByAccount_AccountAndPlId(
                            dto.getAccount(),
                            dto.getPlId());

            //만약 나의 재생목록이 없다면 재생목록을 만든다.
            if(myPlayLists.isEmpty()){
                //나의 새로운 재생 목록을 생성한다.
                Member member = memberRepository.getOne(dto.getAccount());
                AllPlayList newPlayList = AllPlayList.builder()
                        .plName("New PlayList Title")
                        .account(member)
                        .plShare(false) //새로운 재생 목록은 기본적으로 공유하지 않도록 설정을 해둔다.
                        .build();

                //나의 새로운 저장목록을 만든다.
                allPlayListRepository.save(newPlayList);
                //재생목록이 생겼으면 나의 playlist의 새로운 score(악보)들을 담는다.
                PlayList build = PlayList.builder()
                        .pl_id(newPlayList)
                        .score(score.get())
                        .build();
                //나의 새로운 playList를 만든다
                playListRepository.save(build);

                log.info("새로운 저장소와 악보를 추가했습니다. ");

            }else {
                //만약 저장소가 있다면 ?
                AllPlayList selectedPlayList = myPlayLists.get(0);

                // 선택한 재생목록에 악보를 추가한다.
                PlayList build = PlayList.builder()
                        .pl_id(selectedPlayList)
                        .score(score.get())
                        .build();
                playListRepository.save(build);
            }

        }catch (Exception e){
            System.out.println("선택하신 악보는 없는 악보 입니다..");
        }
       return true;
}

    //나의 playList 의 리스트들 불러오기
    public PlayListofListResponseDTO getMyPlayLists(String account, Long plId) {
        // 현재 allPlayList 사용자와 그 사용자가 어떤 재생목록을 사용하는지 가져올 것임
        List<AllPlayList> allPlayLists = allPlayListRepository.findByAccount_AccountAndPlId(account, plId);
        //allPlayList에 계정명과 allPlayList의 ID를 가져왔으면 거기에 해당하는 score들을 가져와야함
        playListRepository.findByPl_id(allPlayLists.)

    }




//            Score score1 = score.get();
//            Optional<AllPlayList> byId = allPlayListRepository.findById(dto.getPlId());
//            AllPlayList allPlayList = byId.get();
//            Member one = memberRepository.getOne(dto.getAccount());
//            if(byId != null) {
//                AllPlayList build = AllPlayList.builder()
//                        .plName("123")
//                        .plShare(false)
//                        .account(one)
//                        .build();
//                allPlayListRepository.save(build);
//                if(score!=null && allPlayList.getAccount().equals(dto.getAccount())){
//                    playListRepository.save(PlayList.builder()
//                                    .
//                                    .score(score1)
//                            .build())
//                }
//            }
//            }
//
//            if(byId != null && score != null && allPlayList.getAccount().equals(dto.getAccount())){
//                playListRepository.save(PlayList.builder()
//                        .pl_id(allPlayList)
//                                .score(score1)
//                        .build());
//            }



