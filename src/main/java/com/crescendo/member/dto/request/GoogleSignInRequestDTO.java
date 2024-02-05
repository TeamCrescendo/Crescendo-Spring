package com.crescendo.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleSignInRequestDTO {
    @NotBlank
    @Size(min = 4 , max = 20)
    private String account;

    private boolean autoLogin;
}
