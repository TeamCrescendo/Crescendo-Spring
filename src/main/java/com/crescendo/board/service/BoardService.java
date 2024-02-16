package com.crescendo.board.service;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.allPlayList.repository.AllPlayListRepository;
import com.crescendo.blackList.entity.BlackList;
import com.crescendo.blackList.repository.BlackListRepository;
import com.crescendo.board.dto.request.BoardModifyRequestDTO;
import com.crescendo.board.dto.request.BoardRequestDTO;
import com.crescendo.board.dto.response.*;
import com.crescendo.board.entity.Board;
import com.crescendo.likeAndDislike.dto.request.LikeAndDislikeRequestDTO;
import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import com.crescendo.likeAndDislike.repository.LikeAndDislikeRepository;
import com.crescendo.member.entity.Member;
import com.crescendo.board.repository.BoardRepository;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.playList.entity.PlayList;
import com.crescendo.playList.repository.PlayListRepository;
import com.crescendo.score.entity.Score;
import com.crescendo.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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
    private final PlayListRepository playListRepository;
    private final AllPlayListRepository allPlayListRepository;
//    private final RestTemplate restTemplate;


    //board에 등록
    public BoardListResponseDTO create(BoardRequestDTO dto, String account) {
        Member member1 = memberRepository.findById(account).orElseThrow();
//        log.info("member1 토큰 저장 {}", member1);
        if (member1 == null) {
            return null;
        }
        Score scoreNo = scoreRepository.findByScoreNo(dto.getScoreNo());
        if (scoreNo == null) {
            return null;
        }
        Board build = Board.builder().boardTitle(dto.getBoardTitle()).member(member1).scoreNo(scoreNo).build();
        boardRepository.save(build);
//        log.info("새로운 보드를 내 마음속에 저★장★ : {}", dto.getBoardTitle());
        return retrieve();
    }

    // board 불러오기
    public BoardListResponseDTO retrieve() {
        List<BoardResponseDTO> dtoList = boardRepository.findAllBoardResponseDTO();
        return BoardListResponseDTO.builder()
                .boards(dtoList)
                .build();
    }

    // 페이징 처리 된 보드 불러오기
    public List<BoardResponseDTO> retrieveWithPage(int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("boardUpdateDateTime").descending());
        Page<Board> result = boardRepository.findAll(pageRequest); // 해당 페이지 리스트
        int totalPages = result.getTotalPages(); // 총 페이지 수
        List<BoardResponseDTO> list = new ArrayList<>();
        result.forEach(board -> {
            BoardResponseDTO build = BoardResponseDTO.builder()
                    .boardNo(board.getBoardNo())
                    .boardTitle(board.getBoardTitle())
                    .boardDownloadCount(board.getBoardDownloadCount())
                    .boardLikeCount(board.getBoardLikeCount())
                    .boardDislikeCount(board.getBoardDislikeCount())
                    .scoreImageUrl(board.getScoreNo().getScoreImageUrl())
                    .scoreNo(board.getScoreNo().getScoreNo())
                    .scoreTitle(board.getScoreNo().getScoreTitle())
                    .memberAccount(board.getMember().getAccount())
                    .boardViewCount(board.getBoardViewCount())
                    .build();
            list.add(build);
        });
        return list;
    }

    // 총 페이지 수 구하기
    public int getAllPageNo(int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("boardUpdateDateTime").descending());
        Page<Board> result = boardRepository.findAll(pageRequest); // 해당 페이지 리스트
        return result.getTotalPages();
    }

    //나의 board 불러오기
    public MyBoardResponseDTO myBoardRetrieve(String account) {
        List<Board> byMemberAccount = boardRepository.findByMember_Account(account);
        List<MyBoardListResponseDTO> myBoardResponseDTO = new ArrayList<>();

        for (Board board : byMemberAccount) {
            if (board.getMember().getAccount().equals(account)) { // 현재 로그인한 사용자의 계정과 게시글의 작성자의 계정이 일치하는 경우
                MyBoardListResponseDTO dto = new MyBoardListResponseDTO(board);
                myBoardResponseDTO.add(dto);
            }
        }
        return MyBoardResponseDTO.builder().boards(myBoardResponseDTO).build();
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

    // board 삭제 처리
    public BoardListResponseDTO delete(String account, Long boardNo) {
        try {
            // 보드를 찾기
            Board board = boardRepository.findByMember_AccountAndAndBoardNo(account, boardNo);
            if (board == null) {
                log.warn("삭제할 보드를 찾을 수 없습니다. 계정: {}, 보드 번호: {}", account, boardNo);
                return null;
            }

            // 보드가 특정 사용자의 플레이 리스트에 담겨져 있는지 확인
            List<PlayList> playlists = playListRepository.findByBoard(board);

            // 보드를 null로 설정한 후에 해당 플레이 리스트를 삭제하고 scoreCount를 감소시킴
            for (PlayList playlist : playlists) {
                playlist.setBoard(null);
                playListRepository.delete(playlist);

                AllPlayList plId = playlist.getPlId();
                plId.setScoreCount(plId.getScoreCount() -1);
                allPlayListRepository.save(plId);
            }

            // 좋아요와 싫어요 데이터를 삭제
            likeAndDislikeRepository.deleteByBoard(board);

            // 보드를 삭제
            boardRepository.deleteById(boardNo);

            log.info("보드 삭제 성공. 계정: {}, 보드 번호: {}", account, boardNo);
        } catch (Exception e) {
            log.error("보드 삭제 중 오류 발생. 계정: {}, 보드 번호: {}", account, boardNo, e);
        }
        return retrieve();
    }






    // 좋아요와 싫어요 기능 처리
    public void LikeAndDislike(LikeAndDislikeRequestDTO dto, String account) {
        // 게시글의 번호를 찾기
        Long boardNo = dto.getBoardNo();
//        log.info("boardNo: {}", boardNo);

        try {
            Optional<Board> boardOptional = boardRepository.findById(boardNo);

            // 게시글을 찾았을 경우
            boardOptional.ifPresent(board -> {
                try {
                    // 게시글의 작성자를 찾기
                    Member member = memberRepository.getOne(account);

                    // 내 계정과 내가 좋아요나 싫어요를 누른 boardNo를 가져옴
                    LikeAndDislike memberAccountAndBoardNo = likeAndDislikeRepository.findByMemberAccountAndBoard_BoardNo(account, boardNo);

                    // 좋아요나 싫어요를 누르지 않은 상태일 때
                    if (memberAccountAndBoardNo == null) {
                        LikeAndDislike likeAndDislike = LikeAndDislike.builder()
                                .boardLike(dto.isLike())
                                .member(member)
                                .board(board)
                                .build();
                        likeAndDislikeRepository.save(likeAndDislike);

                        // 좋아요를 누른 경우
                        if (dto.isLike()) {
                            board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                        }
                        // 싫어요를 누른 경우
                        else {
                            board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                        }

                        // 싫어요 수가 5개 이상인 경우
                        if (board.getBoardDislikeCount() >= 5) {
                            board.setVisible(false);
                            BlackList blackList = BlackList.builder()
                                    .member(member)
                                    .board(board)
                                    .build();
                            blackListRepository.save(blackList);
                        }
                    }
                    // 좋아요나 싫어요를 이미 누른 경우
                    else {
                        // 좋아요를 누른 경우
                        if (dto.isLike()) {
                            // 싫어요에서 좋아요로 변경
                            if (!memberAccountAndBoardNo.isBoardLike()) {
                                board.setBoardLikeCount(board.getBoardLikeCount() + 1);
                                board.setBoardDislikeCount(board.getBoardDislikeCount() - 1);
                            }
                            memberAccountAndBoardNo.setBoardLike(true);
                        }
                        // 싫어요를 누른 경우
                        else {
                            // 좋아요에서 싫어요로 변경
                            if (memberAccountAndBoardNo.isBoardLike()) {
                                board.setBoardLikeCount(board.getBoardLikeCount() - 1);
                                board.setBoardDislikeCount(board.getBoardDislikeCount() + 1);
                            }
                            memberAccountAndBoardNo.setBoardLike(false);
                        }
                    }
                    // 게시글 작성자를 찾을 수 없을 경우
                } catch (EntityNotFoundException e) {
                    System.out.println("게시글 작성자를 찾을 수 없습니다.");
                }
            });
            // 게시글을 찾을 수 없을 경우
        } catch (Exception e) {
            System.out.println("게시글을 찾는 도중 오류가 발생했습니다.");
        }
    }



    //board의 좋아요 수와 싫어요 수 조회
    public BoardLikeAndDisLikeResponseDTO retrieveBoardLikeAndDislikeCount(Long boardNo) {
        try {
            Board byBoardNo = boardRepository.findByBoardNo(boardNo);

            if (byBoardNo == null) {
                log.warn("찾으시는 board는 없는 board 입니다.");
            }
            int likeCount = byBoardNo.getBoardLikeCount();
            int dislikeCount = byBoardNo.getBoardDislikeCount();

            return BoardLikeAndDisLikeResponseDTO.builder()
                    .boardDislikeCount(dislikeCount)
                    .boardLikeCount(likeCount)
                    .build();

        } catch (Exception e) {
            log.error("보드 조회 중 오류 발생. 보드 번호: {}", boardNo, e);
            return null;
        }
    }

    // board 좋아요 싫어요 했는지 여부 체크
    public HashMap<String, Boolean> getClickLikeAndDisLike(String account, Long boardNo) {
        boolean flag = likeAndDislikeRepository.existsByBoard_BoardNoAndMemberAccount(boardNo, account);
        HashMap<String, Boolean> map = new HashMap<>();
        if (flag) {
            LikeAndDislike byMemberAccountAndBoardBoardNo = likeAndDislikeRepository.findByMemberAccountAndBoard_BoardNo(account, boardNo);
            boolean boardLike = byMemberAccountAndBoardBoardNo.isBoardLike();
            map.put("like", boardLike);
        }
        map.put("isClick", flag);
        return map;
    }

    // Board에 조회수 증가 하는 메서드
    public void increaseViewCount(Long boardNo) {
        Board byBoardNo = boardRepository.findByBoardNo(boardNo);
        if (byBoardNo == null) {
            throw new RuntimeException("유효하지 않은 게시판에 접근했습니다");
        }
        byBoardNo.setBoardViewCount(byBoardNo.getBoardViewCount() + 1);
    }

    //board에 누군가가 다운로드를 하면 다운로드 수가 증가 하는 메서드
    public void increaseDownLoadCount(Long boardNo, String account) {

        try {
            //게시글을 조회 해야 함
            Board board = boardRepository.findByBoardNo(boardNo);
            //게시글이 없는 경우엔?
            if (board == null) {
                //내보내기
                throw new RuntimeException("유효하지 않는 게시판 입니다.");
            }
            //다운로드 수 증가 조건
            //board의 작성자와 board의 작성자가 일치 한지 검사를 한다(일치 한다면 다운로드 할 수 있게 해줌).
            //board의 작성자와 board의 작성자가 일치 하지 않는지 검사 한다(일치 하지 않는다면 다운로드 할 수 있게 해줌).
            if (!board.getMember().getAccount().equals(account) || board.getMember().getAccount().equals(account)) {
                board.setBoardDownloadCount(board.getBoardDownloadCount() + 1);
                boardRepository.save(board);
                log.info("다운로드 수가 증가했습니다. boardNo: {}, 다운로드 수: {}", boardNo, board.getBoardDownloadCount());
            }
        } catch (Exception e) {
            log.error("board 다운로드에 실패 했습니다. ");
        }
    }

    //PDF를 가져와서 byte로 변환하여 클라이언트에 전송하는 메서드
    public byte[] getBoardPdf(Long boardId) {
//        try{
        Board byBoardNo = boardRepository.findByBoardNo(boardId);

        String scoreImageUrl = byBoardNo.getScoreNo().getScoreImageUrl();
        RestTemplate restTemplate = new RestTemplate();
//        log.info("123123123");
//        log.info(scoreImageUrl);
//        log.info("123123123");
        byte[] forObject = restTemplate.getForObject(scoreImageUrl, byte[].class);


//            //score로 부터 파일을 읽어서 byte로 변환
//            byte[] bytes = readPdfFile(scoreImageUrl);
//            byte[] pdfBytes = getPdfBytes(scoreImageUrl);
        return forObject;
//            클라이언트에 전송할 HttpHeaders 설정
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
        //PDF 파일 중에 특수문자가 있더라도 안전하게 처리함
//            String FileName = URLEncoder.encode(byBoardNo.getScoreNo().getScoreImageUrl(), "UTF-8");
//            headers.setContentDispositionFormData("attachment", FileName);

        //ResponseEntity를 사용해서 클라이언트에 byte 배열 전송
//            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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





