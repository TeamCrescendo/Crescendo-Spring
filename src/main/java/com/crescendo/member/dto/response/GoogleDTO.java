package com.crescendo.member.dto.response;

import lombok.*;

import javax.persistence.Column;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleDTO {
    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "expires_in")
    private int expiresIn;

    @Column(name = "scope")
    private String scope;


}
