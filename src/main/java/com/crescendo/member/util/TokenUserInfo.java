package com.crescendo.member.util;

import com.crescendo.member.entity.Member;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {
    private String userId;
    private String email;
    private Member.Auth auth;
}
