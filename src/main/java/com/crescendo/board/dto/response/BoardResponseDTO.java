package com.crescendo.board.dto.response;

import com.crescendo.board.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDTO {

    private Long boardNo;

    private String boardTitle;

    private String memberAccount;

    private Integer boardLikeCount;

    private Integer boardDislikeCount;

    private Long boardViewCount;

    private Integer boardDownloadCount;

    private Integer scoreNo;

    private String scoreTitle;

    private String scoreImageUrl;

    private LocalDateTime scoreUploadDateTime;

    public BoardResponseDTO(Board board){
        this.boardNo = board.getBoardNo();
        this.boardTitle = board.getBoardTitle();
        this.memberAccount = board.getMember().getAccount();
        this.boardLikeCount = board.getBoardLikeCount();
        this.boardDislikeCount = board.getBoardDislikeCount();
        this.boardViewCount = board.getBoardViewCount();
        this.boardDownloadCount = board.getBoardDownloadCount();
        this.scoreNo = board.getScoreNo().getScoreNo();
        this.scoreTitle = board.getScoreNo().getScoreTitle();
        this.scoreImageUrl = board.getScoreNo().getScoreImageUrl();
        this.scoreUploadDateTime = board.getScoreNo().getScoreUploadDateTime();
    }
}

