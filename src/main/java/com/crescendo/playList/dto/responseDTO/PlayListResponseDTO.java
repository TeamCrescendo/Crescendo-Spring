package com.crescendo.playList.dto.responseDTO;

import com.crescendo.playList.entity.PlayList;
import com.crescendo.score.entity.Score;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListResponseDTO {

    private Long plNo; //나의 playlist 번호
    private LocalDateTime plAddDateTime; //playList 저장 날짜
    private Long plId; //allPlaylist의 번호
    private List<Score> scoreNo; //나의 악보

    public PlayListResponseDTO(PlayList playList, List<Score> scoreNo){
        this.plNo = playList.getPlNo();
        this.plAddDateTime = playList.getPlAddDateTime();
        this.plId = playList.getPlId().getPlId();
        this.scoreNo = scoreNo;
    }
}
