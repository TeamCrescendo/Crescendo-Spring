package com.crescendo.playList.service;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.playList.dto.requestDTO.PlayListDuplicateRequestDTO;
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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class PlayListService {

    private final MemberRepository memberRepository;
    private final AllPlayListRepository allPlayListRepository;
    private final PlayListRepository playListRepository;
    private final ScoreRepository scoreRepository;
    private final BoardRepository boardRepository;

    public boolean myPlayList(PlayListRequestDTO dto, String account) {
        try {
            // 내 계정을 찾아야 합니다.
            Member member = memberRepository.findById(account).orElseThrow(() -> {
                throw new NoMatchAccountException("아이디가 존재 하지 않습니다");
            });


            // 일단 마음에 드는 score를 가져와야 합니다.
            Score score = scoreRepository.findById(dto.getScoreNo()).orElseThrow(() -> {
                throw new IllegalStateException("악보가 존재 하지 않습니다.");
            });
            log.info("악보를 가져옴 : {}", score);


            // (AllPlayList)플레이리스트 가져옴
            AllPlayList allPlayList = allPlayListRepository.findById(dto.getPlId()).orElseThrow(() -> {
                throw new IllegalStateException("플레이리스트가 존재 하지 않습니다.");
            });

            //board들을 가져옴
            Board board = boardRepository.findByBoardNo(dto.getBoardNo());

            boolean b = playListRepository.existsByPlIdAndScore(allPlayList, score);

            // 중복이 아니면
            if (!b) {
                PlayList save = playListRepository.save(PlayList.builder()
                        .plId(allPlayList)
                        .score(score)
                        .board(board)
                        .build());

                //playList 추가 될 때 오르게 함
                allPlayList.setScoreCount(allPlayList.getScoreCount() + 1);
                allPlayListRepository.save(allPlayList);
                return true;
            }else{
                throw new RuntimeException("중복입니다");
            }

        } catch (Exception e) {
            log.error("본인의 플레이 리스트에 악보를 추가하는 도중 오류가 발생했습니다. {}, {}", dto.getScoreNo(), dto.getPlId(), e);
            return false;
        }
    }



    // 나의 playList조회
    public List<PlayListResponseDTO> findMyPlayList(String account, Long plId) {
        return playListRepository.findByPlNoAndAndPlAddDateTimeAndPlIdAndScore(account, plId);
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
                allPlayList.setScoreCount(allPlayList.getScoreCount() - 1);
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


    public List<Boolean> duplicationCheckDTO(PlayListDuplicateRequestDTO dto) {
        log.info("정상수: {}", dto.toString());
        List<PlayListRequestDTO> list = dto.getList();
        List<Boolean> list1 = new ArrayList<>();
        list.forEach(playListRequestDTO -> {
            boolean flag = duplicationCheck(playListRequestDTO.getPlId(), playListRequestDTO.getScoreNo());
            list1.add(flag);
        });
        return list1;
    }

    // 중복 체크
    public boolean duplicationCheck(Long plId, int scoreNo){

        Score score = scoreRepository.findById(scoreNo).orElseThrow(() -> {
            throw new IllegalStateException("악보가 존재 하지 않습니다.");
        });


        // (AllPlayList)플레이리스트 가져옴
        AllPlayList allPlayList = allPlayListRepository.findById(plId).orElseThrow(() -> {
            throw new IllegalStateException("플레이리스트가 존재 하지 않습니다.");
        });

        return playListRepository.existsByPlIdAndScore(allPlayList, score);
    }
}
