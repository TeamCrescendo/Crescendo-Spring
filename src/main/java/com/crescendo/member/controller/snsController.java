package com.crescendo.member.controller;

import com.crescendo.member.dto.response.LoginUserResponseDTO;
import com.crescendo.member.exception.IncorrectPasswordException;
import com.crescendo.member.exception.NoLoginArgumentsException;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.service.snsLoginService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/oauth2")
@CrossOrigin(origins = {"http://localhost:3000"})
public class snsController {

    private final snsLoginService snsLoginService;

    @PostMapping("/google/info")
    public ResponseEntity<?> getAccessToken(@RequestBody String accessToken,HttpSession session) throws IOException {
        LoginUserResponseDTO loginUserResponseDTO = snsLoginService.googleLoginByaccess(accessToken);
//        session.setAttribute("login", loginUserResponseDTO);
//        session.setMaxInactiveInterval(60 * 60);
        return ResponseEntity.ok().body(loginUserResponseDTO);


    }

}





