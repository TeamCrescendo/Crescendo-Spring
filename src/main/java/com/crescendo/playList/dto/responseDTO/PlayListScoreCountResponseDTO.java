package com.crescendo.playList.dto.responseDTO;

import com.crescendo.allPlayList.entity.AllPlayList;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListScoreCountResponseDTO {

    private int scoreCount;

    public PlayListScoreCountResponseDTO(AllPlayList allPlayList){
        this.scoreCount = allPlayList.getScoreCount();
    }
}
