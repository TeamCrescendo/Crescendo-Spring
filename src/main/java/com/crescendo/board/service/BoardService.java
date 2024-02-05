package com.crescendo.board.service;

import com.crescendo.blackList.entity.BlackList;
import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.board.dto.response.BoardResponseDTO;
import com.crescendo.board.entity.Board;
import com.crescendo.likeAndDislike.dto.request.LikeAndDislikeRequestDTO;
import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import com.crescendo.likeAndDislike.repository.LikeAndDislikeRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.score.entity.Score;
import com.crescendo.score.repository.ScoreRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // !! JPA 사용시 필수!!
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;
    private final LikeAndDislikeRepository likeAndDislikeRepository;
    private final ScoreRepository scoreRepository;


    //board에 등록
    public BoardListResponseDTO create(BoardRequestDTO dto, String account) {
        Member member1 = memberRepository.findById(account).orElseThrow();
        log.info("member1 토큰 저장 {}", member1);
        if (member1 == null) {
            return null;
        }
        Score scoreNo = scoreRepository.findByScoreNo(dto.getScoreNo());
        if (scoreNo == null) {
            return null;
        }
        Board build = Board.builder().boardTitle(dto.getBoardTitle()).member(member1).scoreNo(scoreNo).build();
        boardRepository.save(build);
        log.info("새로운 보드를 내 마음속에 저★장★ : {}", dto.getBoardTitle());
        return retrieve();
    }

    // board 불러오기
    public BoardListResponseDTO retrieve() {
        List<BoardResponseDTO> dtoList = boardRepository.findAllBoardResponseDTO();
        return BoardListResponseDTO.builder()
                .boards(dtoList)
                .build();
    }

    //board 수정
    public boolean modifyBoard(BoardModifyRequestDTO dto) {
        Board board = boardRepository.getOne(dto.getBoardNo());
        if (!board.getMember().getAccount().equals(dto.getAccount())) {
            return false;
        }
        board.setBoardTitle(dto.getBoardTitle());
        board.setScoreNo(dto.getScoreNo());

        return true;
    }

    //board 삭제 처리
    public BoardListResponseDTO delete(String member) {

        try {
            boardRepository.findByMember_Account(member);
        } catch (Exception e) {
            log.error("board의 번호가 존재하지 않아서 삭제에 실패 했습니다. -{}, error : {}",
                    member, e.getMessage());
            throw new RuntimeException("삭제에 실패 하셨습니다.");
        }
        return retrieve();
    }


    //좋아요와 싫어요 기능 처리
    public void LikeAndDislike(LikeAndDislikeRequestDTO dto,String account) {

        //게시글의 번호를 찾기
        Long boardNo = dto.getBoardNo();
        log.info("boarNo : {}", boardNo);
        try {
            Optional<Board> boardId = boardRepository.findById(boardNo);

            //게시글을 찾으면 이제 게시글 작성자를 찾아야 한다.
            boardId.ifPresent(board -> {
                //게시글 에서 작성자 찾기
                try {
                    //게시글에서 작성자와 member테이블의 member와 비교해서 찾음
                    Member member = memberRepository.getOne(account);
                    //나의 좋아요 수와 싫어요 수를 확인 하기 위해 ,
                    //내 계정과 내가 좋아요나 싫어요를 누른 boardNo를 가져옴
                    LikeAndDislike memberAccountAndBoardNo =
                            likeAndDislikeRepository.findByMemberAccountAndBoard_BoardNo(
                                    dto.getAccount(), dto.getBoardNo());

                    //만약에 좋아요나 싫어요를 누르지 않은 상태라면,
                    //좋아요를 생성함
                    if (memberAccountAndBoardNo == null) {
                        LikeAndDislike build = LikeAndDislike.builder()
                                .boardLike(dto.isLike())
                                .member(member)
                                .board(board)
                                .build();
                        likeAndDislikeRepository.save(build);
                        //만약에 좋아요를 누른 상태라면?
                        if (dto.isLike()) {
                            //그 게시물에 좋아요 + 1 추가
                            board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                        } else {
                            //만약에 좋아요를 누르지 않은 상태라면 ? 싫어요 +1 추가
                            board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                        }
                        //아니면
                    } else {
                        //내가 좋아요를 눌렀을 때, 좋아요가 눌러져 있지 상태라면?
                        if (dto.isLike()) {
                            if (!memberAccountAndBoardNo.isBoardLike()) {
                                board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                                board.setBoardDislikeCount(board.getBoardDislikeCount() - 1);
                            }
                            //아니면 내가 싫어요를 눌렀을때, 좋아요를 눌러져 있는 상태라면?
                        } else {
                            if (memberAccountAndBoardNo.isBoardLike()) {
                                board.setBoardLikeCount(board.getBoardLikeCount() - 1);
                                board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                            } else {
                                //board의 dislikecount가 5이상이면 blacklist 테이블에 추가하기
                                if (board.getBoardDislikeCount() >= 5) {
                                    System.out.println("싫어요 5개 달성");
                                    BlackList build = BlackList.builder()
                                            .board(board)
                                            .member(member)
                                            .build();
                                    blackListRepository.save(build);
                                }
                            }
                        }
                    }
                    //게시글의 작성자를 찾을 수가 없다.
                } catch (EntityNotFoundException e) {
                    System.out.println("작성자를 찾을 수 없습니다");
                }
            });
            //게시글이 없다.
        } catch (Exception e) {
            System.out.println("게시글을 찾는 도중 오류가 발생 했습니다.");
        }
    }

    //PDF를 가져와서 byte로 변환하여 클라이언트에 전송하는 메서드
    public ResponseEntity<byte[]> getBoardPdf(Long boardId){
        try{
            Board board = boardRepository.findByBoardNo(boardId);
            String score = board.getScoreNo().getScoreImageUrl();

            //score로 부터 파일을 읽어서 byte로 변환
            byte[] bytes = readPdfFile(score);

            //클라이언트에 전송할 HttpHeaders 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            //PDF 파일 중에 특수문자가 있더라도 안전하게 처리함
            String FileName = URLEncoder.encode(board.getScoreNo().getScoreImageUrl(), "UTF-8");
            headers.setContentDispositionFormData("attachment",FileName);

            //ResponseEntity를 사용해서 클라이언트에 byte 배열 전송
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // pdf 파일을 읽어 byte 배열로 변환하는 메서드
    private static byte[] readPdfFile(String pdfPath) throws IOException {
        try (InputStream inputStream = new FileInputStream(pdfPath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}

//


