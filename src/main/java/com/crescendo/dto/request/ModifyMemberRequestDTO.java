package com.crescendo.dto.request;

import com.crescendo.entity.Member;
import lombok.*;



@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyMemberRequestDTO {
    private String account;
    private String userName;
    private String password;
    private String email;
}
