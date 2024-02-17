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
                .allowedOrigins("http://localhost:3000","http://cresendo.site" ,"http://crescendo-react-bucket.s3-website.ap-northeast-2.amazonaws.com") // 어떤 클라이언트를 허용할 지
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 어떤 요청 방식을 허용할 지
                .allowedHeaders("*") // 어떤 헤더를 허용할 지
                .exposedHeaders("score-id")
                .allowCredentials(true) // 쿠키 전달을 허용할 지 (토큰 도)
                .maxAge(3600) // 허용시간에 대한 캐싱 설정 1시간 마다
        ;

    }

}
