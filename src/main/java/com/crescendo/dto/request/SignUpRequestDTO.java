package com.crescendo.dto.request;

import com.crescendo.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDTO {
    @NotBlank
    @Size(min = 4 , max = 20)
    private String account;

    @NotBlank
    @Size(min = 4 , max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 12)
    private String userName;

    @NotBlank
    @Email
    @Size(min = 2, max = 30)
    private String email;


    public Member toEntity(){
        return Member.builder()
                .account(account)
                .password(password)
                .userName(userName)
                .email(email)
                .build();
    }
}
