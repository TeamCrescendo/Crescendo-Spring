package com.crescendo.dto.request;

import com.crescendo.entity.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Size(min = 4 , max = 100)
    private String password;

    @NotBlank
    @Size(min = 2, max = 12)
    private String userName;

    @NotBlank
    @Email
    @Size(min = 2, max = 30)
    private String email;


    public Member toEntity(PasswordEncoder encoder){
        return Member.builder()
                .account(account)
                .password(encoder.encode(password))
                .userName(userName)
                .email(email)
                .build();
    }
}
