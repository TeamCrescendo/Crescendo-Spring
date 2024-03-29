package com.crescendo.member.util;

import com.crescendo.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenProvider {
    // 서명할 때 사용하는 비밀 키 ( 512bit 이상의 랜덤 문자열)
    @Value("${jwt.secret}")
    private String SECRET_KEY;




    public String createAutiLoginJwt(Member userEntity) {
        Date expiry = Date.from(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        // 추가 클레임 정의
        Map<String, Object> claims = new HashMap<>();
        claims.put("account", userEntity.getAccount());
        claims.put("auth", userEntity.getAuth());
        return Jwts.builder()
                // token header에 들어갈 서명
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512)
                // token payload에 들어갈 클레임 설정
                .setClaims(claims) // 추가 클레임(사용자 지정)은 젤 위에 지정
                .setIssuer("조 바이든") // iss: 발급자 정보
                .setIssuedAt(new Date()) // iat: 발급시간
                .setExpiration(expiry) // exp: 만료시간
                .setSubject(userEntity.getAccount())  // sub: 토큰을 식별할 수 있는 주요데이터
                .compact();
    }



    public String createToken(Member userEntity) {
        // 토큰 만료 시간 생성
        Date expiry = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );


        // 추가 클레임 정의
        Map<String, Object> claims = new HashMap<>();
        claims.put("account", userEntity.getAccount());
        claims.put("auth", userEntity.getAuth());


        return Jwts.builder()
                // token header에 들어갈 서명
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )
                // token payload에 들어갈 클레임 설정
                .setClaims(claims) // 추가 클레임(사용자 지정)은 젤 위에 지정
                .setIssuer("조 바이든") // iss: 발급자 정보
                .setIssuedAt(new Date()) // iat: 발급시간
                .setExpiration(expiry) // exp: 만료시간
                .setSubject(userEntity.getAccount())  // sub: 토큰을 식별할 수 있는 주요데이터
                .compact();
    }

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조여부를 확인
     * 토큰을 json으로 파싱해서 클레임(토큰정보)를 리턴
     * @param token
     * @return - 토큰 안에있는 인증된 유저정보를 반환
     */
    public TokenUserInfo validateAndGetTokenUserInfo(String token) {

        Claims claims = Jwts.parserBuilder()
                // 토큰 발급자의 발급 당시의 서명을 넣어줌
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                // 서명 위조 검사: 위조된경우 예외가 발생합니다.
                // 위조가 되지 않은 경우 페이로드를 리턴
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claims: {}", claims);

        return TokenUserInfo.builder()
                .userId(claims.getSubject())
                .account(claims.get("account", String.class))
                .auth(Member.Auth.valueOf(claims.get("auth", String.class)))
                .build();
    }

}
