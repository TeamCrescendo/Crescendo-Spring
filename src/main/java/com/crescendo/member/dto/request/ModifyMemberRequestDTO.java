package com.crescendo.member.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ModifyMemberRequestDTO {
    @NotBlank
    @Size(min = 4 , max = 20)
    private String account;

    @NotBlank
    @Size(min = 2, max = 12)
    private String userName;


    private String password;

    @NotBlank
    @Email
    @Size(min = 2, max = 30)
    private String email;

    private MultipartFile file;

}
