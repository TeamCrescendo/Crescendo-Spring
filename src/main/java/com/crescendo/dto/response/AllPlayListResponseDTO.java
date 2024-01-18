package com.crescendo.dto.response;

import com.crescendo.entity.AllPlayList;
import com.crescendo.entity.Member;
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

    public AllPlayListResponseDTO(AllPlayList allPlayList){
        this.member= allPlayList.getAccount();
        this.plId = allPlayList.getPlId();
        this.plName = allPlayList.getPlName();
        this.plShare = allPlayList.isPlShare();
    }

}
