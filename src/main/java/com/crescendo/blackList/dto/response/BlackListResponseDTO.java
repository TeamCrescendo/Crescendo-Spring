package com.crescendo.blackList.dto.response;

import com.crescendo.blackList.entity.BlackList;
import com.crescendo.member.entity.Member;
import lombok.*;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListResponseDTO {

    private Long boardId;

    private Long boardNo;

    private Member account;

    public BlackListResponseDTO(BlackList blackList){
        this.boardNo = blackList.getBlackId();
        this.boardId = blackList.getBlackId();
        this.account = blackList.getMember();
    }
}
