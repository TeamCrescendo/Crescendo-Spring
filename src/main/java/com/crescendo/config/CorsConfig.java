package com.crescendo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**") // 어떤 요청 URL을 허용할 지
                .allowedOrigins("http://localhost:3000","http://localhost:8484","https://accounts.google.com/o/oauth2/v2/auth"
                        ,"http://localhost:8484/api/auth/oauth2/googleInfo") // 어떤 클라이언트를 허용할 지
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 어떤 요청 방식을 허용할 지
                .allowedHeaders("*") // 어떤 헤더를 허용할 지
                .exposedHeaders("score-id")
                .allowCredentials(true) // 쿠키 전달을 허용할 지 (토큰 도)
                .maxAge(3600) // 허용시간에 대한 캐싱 설정 1시간 마다
        ;

        registry.addMapping("/api/auth/oauth2/googleInfo")
                .allowedOrigins("http://localhost:3000","https://oauth2.googleapis.com/token","https://www.googleapis.com/oauth2/v2/userinfo")  // 허용할 오리진
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 인증 정보 허용
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("http://localhost:8484/api/auth/oauth2/googleInfo/**")
                ;
    }
}
