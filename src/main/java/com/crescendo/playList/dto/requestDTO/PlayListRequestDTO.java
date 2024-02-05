package com.crescendo.playList.dto.requestDTO;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.score.entity.Score;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListRequestDTO {
    private Long plId;
    private int scoreNo;
//    private String account;
}
