package com.crescendo.dto.request;

import com.crescendo.entity.Board;
import com.crescendo.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
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

    private Member member;




    public Board toEntity(){
        return Board.builder()
                .boardTitle(boardTitle)
                .member(member)
                .build();
    }

}

