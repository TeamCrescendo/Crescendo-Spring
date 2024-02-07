package com.crescendo.playList.service;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.playList.dto.requestDTO.PlayListRequestDTO;
import com.crescendo.playList.dto.responseDTO.PlayListResponseDTO;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class PlayListService {

    private final MemberRepository memberRepository;
    private final AllPlayListRepository allPlayListRepository;
    private final PlayListRepository playListRepository;
    private final ScoreRepository scoreRepository;

    public boolean myPlayList(PlayListRequestDTO dto, String account) {
        try {
            // 내 계정을 찾아야 합니다.
            Member member = memberRepository.getOne(account);
            if (member == null) {
                log.warn("본인의 계정이 아닙니다.");
                return false; // 계정을 찾지 못한 경우 false를 반환합니다.
            }

            // 일단 마음에 드는 score를 가져와야 합니다.
            Score score = scoreRepository.findByScoreNo(dto.getScore());
            log.info("악보를 가져옴 : {}", score);
            // 악보가 없을 경우
            if (score == null) {
                log.warn("선택하신 악보는 없는 악보입니다. {}", dto.getScore());
                return false;
            }

            // 나의 (AllPlayList)재생목록들을 가져와야 합니다.
            List<AllPlayList> myPlayLists = allPlayListRepository.findByAccount_AccountAndPlId(account, dto.getPlId());
            if (!account.equals(member.getAccount())) {
                log.warn("본인의 재생목록이 아닙니다.");
                return false;
            }

            if (myPlayLists.isEmpty()) {
                AllPlayList newMyPlayList = AllPlayList.builder()
                        .plShare(false)
                        .account(member)
                        .plName("New My PlayList Title")
                        .build();

                // 그리고 새로운 AllPlayList에 악보를 넣습니다.
                PlayList playList = PlayList.builder()
                        .plId(newMyPlayList)
                        .score(score)
                        .build();
                playListRepository.save(playList);

                // AllPlayList의 scoreCount를 1 증가시킵니다.
                newMyPlayList.setScoreCount(newMyPlayList.getScoreCount() + 1);
                allPlayListRepository.save(newMyPlayList);

                return true;
            } else {


                // 기존 재생목록에 악보를 추가하기 전에 중복 체크를 합니다.
                AllPlayList selectPlayList = myPlayLists.get(0);
                List<PlayList> scoreList = playListRepository.findByPlIdAndScore(dto.getPlId(), dto.getScore());
                if (scoreList == null) {
                    log.warn("당신의 플레이 리스트에 선택하신 악보는 이미 있습니다.");
                    return false;
                }

                // 중복 체크에서 악보가 없으면 해당 재생목록에 악보를 추가합니다.
                PlayList intoScore = PlayList.builder()
                        .plId(selectPlayList)
                        .score(score)
                        .build();
                playListRepository.save(intoScore);

                // AllPlayList의 scoreCount를 1 증가시킵니다.
                selectPlayList.setScoreCount(selectPlayList.getScoreCount() + 1);
                allPlayListRepository.save(selectPlayList);
                log.info("allPlayList의 scoreCount가 증가");

                return true;
            }
        } catch (Exception e) {
            log.error("본인의 플레이 리스트에 악보를 추가하는 도중 오류가 발생했습니다. {}, {}", dto.getScore(), dto.getPlId(), e);
            return false;
        }
    }

    // 나의 playList조회
    public List<PlayListResponseDTO> findMyPlayList(String account){
        return playListRepository.findByPlNoAndAndPlAddDateTimeAndPlIdAAndScore(account);
    }

    // 나의 playList 안에 score를 삭제하고 나의 playList 조회 결과를 반환
    public boolean deleteMyPlayListAndRetrieve(String account, Long plNo) {
        try {
            // 해당 playList를 가져옵니다.
            playListRepository.findById(plNo).ifPresent(playList -> {
                AllPlayList allPlayList = playList.getPlId();

                // 해당 playList가 현재 사용자의 것인지 확인합니다.
                if (allPlayList == null || allPlayList.getAccount() == null || !account.equals(allPlayList.getAccount().getAccount())) {
                    log.warn("본인의 플레이 리스트가 아닙니다. playList ID: {}", plNo);
                    return;
                }

                // playList를 삭제합니다.
                playListRepository.delete(playList);

                // allPlayList의 scoreCount를 1 감소시킵니다.
                allPlayList.setScoreCount(allPlayList.getScoreCount() -1);
                allPlayListRepository.save(allPlayList);

                log.info("playList를 성공적으로 삭제했습니다. playList ID: {}", plNo);
            });

            // 삭제가 성공했으므로 true를 반환합니다.
            return true;

        } catch (Exception e) {
            // 예외가 발생했을 때 예외 정보를 로그로 출력합니다.
            log.error("삭제를 하던 도중 오류가 발생했습니다. playList ID: {}", plNo, e);
            // 삭제가 실패했으므로 false를 반환합니다.
            return false;
        }
    }


}
