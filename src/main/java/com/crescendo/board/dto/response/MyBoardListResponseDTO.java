package com.crescendo.board.dto.response;

import com.crescendo.board.entity.Board;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyBoardListResponseDTO {

    private Long boardNo;

    private String boardTitle;

    private String memberAccount;

    private Integer boardLikeCount;

    private Integer boardDislikeCount;

    private Long boardViewCount;

    private Integer boardDownloadCount;

    public MyBoardListResponseDTO(Board board){
        this.boardNo = board.getBoardNo();
        this.boardTitle = board.getBoardTitle();
        this.memberAccount =board.getMember().getAccount();
        this.boardLikeCount = board.getBoardLikeCount();
        this.boardDislikeCount = board.getBoardDislikeCount();
        this.boardViewCount = board.getBoardViewCount();
        this.boardDownloadCount = board.getBoardDownloadCount();
    }
}
