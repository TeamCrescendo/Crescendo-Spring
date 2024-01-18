package com.crescendo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
    @Bean // 라이브러리 클래스같은 내가만들지 않은 객체를 등록해서 주입받기 위한 아노테이션
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable() // CSRF 토큰공격을 방지하기위한 장치 해제
                // 모든요청에 대해서 인증을 하지 않겠다.
                .authorizeRequests().antMatchers("/**").permitAll();

        return http.build();
    }

    // 비밀번호 암호화 객체를 빈 등록
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
