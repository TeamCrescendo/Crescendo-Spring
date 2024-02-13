package com.crescendo.member.dto.request;

import com.crescendo.member.entity.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

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


    private String password;

    @NotBlank
    @Size(min = 2, max = 12)
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "유저 이름은 한국어 또는 영어만 허용됩니다.")
    private String userName;

    @NotBlank
    @Email
    @Size(min = 2, max = 30)
    private String email;

    private MultipartFile profileImage;


    public Member toEntity(PasswordEncoder encoder){
        return Member.builder()
                .account(account)
                .password(encoder.encode(password))
                .userName(userName)
                .email(email)
                .build();
    }
}
