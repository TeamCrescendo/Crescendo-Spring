package com.crescendo.allPlayList.dto.response;

import com.crescendo.allPlayList.entity.AllPlayList;
import com.crescendo.member.entity.Member;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllPlayListResponseDTO {

    private Member member;
    private Long plId;
    private String plName;
    private boolean plShare;
    private int scoreCount;

    public AllPlayListResponseDTO(AllPlayList allPlayList){
        this.member= allPlayList.getAccount();
        this.plId = allPlayList.getPlId();
        this.plName = allPlayList.getPlName();
        this.plShare = allPlayList.isPlShare();
        this.scoreCount = getScoreCount();
    }

}
