package com.crescendo.allPlayList.dto.request;

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
    private Long plId;

//    public AllPlayList toEntity(){
//        return AllPlayList.builder()
//                .plName(plName)
//                .account(account);
//                .plShare(plShare)
//                .build();
//    }
;
}
