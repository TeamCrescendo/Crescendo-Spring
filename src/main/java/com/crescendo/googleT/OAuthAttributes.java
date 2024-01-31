package com.crescendo.googleT;

import com.crescendo.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter

public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String nickname;
    private String email;
    private Member.Auth role;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        /* 구글인지 네이버인지 카카오인지 구분하기 위한 메소드 (ofNaver, ofKaKao) */

        return ofGoogle(userNameAttributeName, attributes);
    }
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder().username((String) attributes.get("email"))
                .email((String) attributes.get("email"))
        .nickname((String) attributes.get("name"))
        .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

        public Member toEntity() {
            return Member.builder()
                    .account(email)
                    .email(email)
                    .userName(nickname)
                    .build();
        }






}
