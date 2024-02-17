package com.crescendo.Util;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.blackList.entity.BlackList;
import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.dto.response.MyBoardResponseDTO;
import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.board.service.BoardService;
import com.crescendo.inquiry.entity.Inquiry;
import com.crescendo.inquiry.repository.InquiryRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.post_message.entity.PostMessage;
import com.crescendo.post_message.repository.PostMessageRepository;
import com.crescendo.restore.entity.Restore;
import com.crescendo.restore.repository.RestoreRepository;
import com.crescendo.score.entity.Score;
import com.crescendo.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteScheduler {
    private final RestoreRepository restoreRepository;
    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final ScoreRepository scoreRepository;
    private final EntityManager entityManager;
    private final BoardRepository boardRepository;
    private final AllPlayListRepository allPlayListRepository;
    private final PostMessageRepository postMessageRepository;
    private final BlackListRepository blackListRepository;
    private final BoardService boardService;

    @Scheduled(fixedRate = 1000 * 60 * 1) // 1분 마다
    @Transactional
    public void performDeleteTask() {
        List<Restore> restoreByOverTime = restoreRepository.getRestoreByOverTime();
        if (restoreByOverTime != null) {
            for (Restore restore : restoreByOverTime) {
                String account = restore.getMember().getAccount();
                deleteAllPlayList(account);
                deletePostMessage(account);
                deleteBlackList(account);
                changeInquiryMemberToNull(account);
                changeScoreMemberToNull(account);
                changeBoardMemberToNull(account);

                restoreRepository.deleteById(restore.getRestoreNo());
                Member member = restore.getMember();
                memberRepository.delete(member);
                log.info("삭제했냐 ?");
            }
        }

    }

    // 문의 member null 로 바꾸기
    @Transactional
    public void changeInquiryMemberToNull(String account) {
        List<Inquiry> allByMemberAccountOrderByInquiryDateTimeDesc = inquiryRepository.findAllByMemberAccountOrderByInquiryDateTimeDesc(account);
        inquiryRepository.deleteAll(allByMemberAccountOrderByInquiryDateTimeDesc);
    }

    // 악보 member null 로 바꾸기
    @Transactional
    public void changeScoreMemberToNull(String account){
        List<Score> allByMemberAccount = scoreRepository.findAllByMemberAccount(account);
        allByMemberAccount.forEach(score -> {
            score.setMember(null);
        });
    }

    // 올 플리 삭제 하기
    @Transactional
    public void deleteAllPlayList(String account){
        List<AllPlayList> allByAccount = allPlayListRepository.findByAccount_Account(account);
        allPlayListRepository.deleteAll(allByAccount);
    }

    // 게시판 삭제하기
    @Transactional
    public void changeBoardMemberToNull(String account){
        List<Board> allByMemberAccount = boardRepository.findAllByMemberAccount(account);
        if(allByMemberAccount.isEmpty()){
            log.info("비어있음 ㅋㅋ");
        }else{
            log.info("안 비어있어 씨발");
            log.info("그러면: {}", allByMemberAccount);
        }
        if(!allByMemberAccount.isEmpty()){
            allByMemberAccount.forEach(board -> {
                log.info("게시판쪽");
                boardService.delete(account, board.getBoardNo());
            });
        }
    }

    @Transactional
    public void deletePostMessage(String account){
        List<PostMessage> aLlByMemberAccount = postMessageRepository.findALlByMemberAccount(account);
        postMessageRepository.deleteAll(aLlByMemberAccount);
    }

    @Transactional
    public void deleteBlackList(String account){
        List<BlackList> allByMemberAccount = blackListRepository.findAllByMemberAccount(account);
        blackListRepository.deleteAll(allByMemberAccount);
    }
}
