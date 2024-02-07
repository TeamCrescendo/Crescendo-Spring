package com.crescendo.board.dto.request;

import com.crescendo.score.entity.Score;
import lombok.*;

import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDTO {


    @Size(min = 2, max = 45)
    private String boardTitle;

//    private String account;

    private int scoreNo;





//    public Board toEntity(){
//        return Board.builder()
//                .boardTitle(boardTitle)
//                .member(account)
//                .build();
//    }

}

