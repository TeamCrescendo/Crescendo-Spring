package com.crescendo.board.dto.request;

import com.crescendo.score.entity.Score;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModifyRequestDTO {

    private Long boardNo;
    private String account;
    private String boardTitle;
    private Score scoreNo;

}
