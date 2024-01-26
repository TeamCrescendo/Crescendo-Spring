package com.crescendo.report.service;

import com.crescendo.board.entity.Board;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.report.dto.requestDTO.ReportRepuestDTO;
import com.crescendo.report.dto.responseDTO.ReportResponseDTO;
import com.crescendo.report.entity.Report;
import com.crescendo.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ReportService {

    private BoardRepository boardRepository;
    private MemberRepository memberRepository;
    private ReportRepository reportRepository;

    //게시물 신고 하는거 만들기
    public boolean ReportCreate(ReportRepuestDTO dto){
        //일단 나의 계정을 가져와야 한다.
        try{
            Member member = memberRepository.getOne(dto.getReportReporter().getAccount());
            //내 계정이 없다면 내보내기
            if(member ==null){
                System.out.println("사용자가 없습니다.");
                return false;
            }
            //그러고 내가 신고할 게시물을 가져와야 한다.
            Board board = boardRepository.findByBoardNo(dto.getBoardNo().getBoardNo());
            //게시물이 없다면 내보내기
            if(board ==null){
                System.out.println("신고할 게시물이 없군요.");
                return false;
            }
            //나의 계정과 신고할 게시물이 있다면, 신고를 해야한다.
            if(member != null && board != null){
                Report report1 = Report.builder()
                        .reportContent("New Content")
                        .boardNo(board)
                        .reportReporter(member)
                        .build();
                reportRepository.save(report1);
            }else {
                System.out.println("게시글 신고가 정상적으로 성공하지 못했습니다.");
            }
        }catch (Exception e){
            System.out.println("신고할 수가 없습니다.");
            return false;
        }
        return true;
    }
//    //내가 작성한 신고 글들 확인하기
//    public ReportResponseDTO reportResponseDTO(String account,Long reportId){
//
//        //내 계정을 꺼내온다.
//        Member member = memberRepository.getOne(account);
//
//        //신고글을 꺼내온다.
//        List<Report> report = reportRepository.findByReportNo(reportId);
//
//        //내가 작성한 신고글 들을 조회 한다.
//        report.stream()
//                .map(ReportResponseDTO::new)
//                .collect(Collectors.toList());
//
//        return ReportResponseDTO.builder()
//                .reportReporter(member)
//                .build();
//    }


}
