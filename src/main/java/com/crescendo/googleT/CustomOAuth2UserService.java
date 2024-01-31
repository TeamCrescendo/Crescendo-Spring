package com.crescendo.googleT;

import com.crescendo.member.entity.Member;
import com.crescendo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/** * Security UserDetailsService == OAuth OAuth2UserService * */

@RequiredArgsConstructor
@Service

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest , OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /*Oauth 서비스 id 구분코드 구글, 카카오 네이버 */
       String registrationId = userRequest.getClientRegistration().getRegistrationId();
       /* OAuth2 로그인 진행시 키가 되는 필드 값 (PK) (구글의 기본 코드는 "sub") */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        /* oauth2  Userservice*/
        OAuthAttributes attributes=OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        Member member=saveOrUpdate(attributes);
        System.out.println("member = " + member);
        /* 세셩 정보를 저장하는 직렬화된 dto 클래스*/


        return  null;


    }
    /*
    소셜 로그인시 기존 회원이 존재하면 기존 데이터 그대로 보존
     */
    private Member saveOrUpdate(OAuthAttributes attributes){
        Member member = memberRepository.findMemberByEmail(attributes.getEmail());
        if(member!=null) return member;
        else {
            Member newMember = attributes.toEntity();
            return memberRepository.save(newMember);
        }
    }
}
