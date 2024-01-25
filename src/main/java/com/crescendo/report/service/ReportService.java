package com.crescendo.report.service;

import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.report.dto.requestDTO.ReportRepuestDTO;
import com.crescendo.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ReportService {

    private BoardRepository boardRepository;
    private MemberRepository memberRepository;
    private ReportRepository reportRepository;

    public boolean ReportCreate(ReportRepuestDTO dto){
        //일단 나의 계정을 가져와야 한다.
        try{
            Member member = memberRepository.getOne(dto.getReportReporter());
            if(member ==null){
                System.out.println("사용자가 없습니다.");
                return false;
            }
            //그러고 내가 신고할 게시물을 가져와야 한다.
            Optional<Board> board = boardRepository.findById(dto.getBardNo());
            if(board ==null){
                System.out.println("신고할 게시물이 없군요.");
                return false;
            }
            //나의 계정과 신고할 게시물이 있다면, 신고를 해야한다.
            if(member != null && board != null){
                reportRepository.save();
            }

        }


    }
}
