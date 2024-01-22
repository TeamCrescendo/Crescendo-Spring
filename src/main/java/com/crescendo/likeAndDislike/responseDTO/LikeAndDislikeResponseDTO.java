package com.crescendo.likeAndDislike.responseDTO;

import com.crescendo.board.entity.Dislike;
import com.crescendo.board.entity.Like;
import com.crescendo.likeAndDislike.entity.LikeAndDislike;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeAndDislikeResponseDTO {

    private Long likeAndDislikeId;
    private Dislike boardDisLike;
    private Like boardLike;
    private Long boardNo;
    private String account;

    public LikeAndDislikeResponseDTO(LikeAndDislike likeAndDislike){
        this.likeAndDislikeId = likeAndDislike.getLikeAndDislikeId();
        this.boardDisLike  = likeAndDislike.getBoardDislike();
        this.boardLike = likeAndDislike.getBoardLike();
        this.boardNo = this.getBoardNo();
        this.account = this.getAccount();
    }
}
