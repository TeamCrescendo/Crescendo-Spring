package com.crescendo.dto.request;

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
public class AllPlayListRequestDTO {

    private String plName;
    private String account;
    private boolean plShare;

//    public AllPlayList toEntity(){
//        return AllPlayList.builder()
//                .plName(plName)
//                .account(account);
//                .plShare(plShare)
//                .build();
//    }
;
}
