package com.crescendo.board.dto.response;

import com.crescendo.board.entity.Board;
import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.member.entity.Member;
import lombok.*;

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

    private Member member;

    private Like boardLike;

    private Dislike boardDislike;

    private Long boardViewCount;

    private Integer boardDownloadCount;

    private String scoreImgUrl;

    public BoardResponseDTO(Board board){
        this.boardNo = board.getBoardNo();
        this.boardTitle = board.getBoardTitle();
        this.member = board.getMember();
        this.boardLike = board.getBoardLike();
        this.boardDislike = board.getBoardDislike();
        this.boardViewCount = board.getBoardViewCount();
        this.boardDownloadCount = board.getBoardDownloadCount();
        this.scoreImgUrl = board.getScoreImgUrl();
    }
}

