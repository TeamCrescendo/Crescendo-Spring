package com.crescendo.post_message.controller;

import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.post_message.dto.request.SendMessageRequestDTO;
import com.crescendo.post_message.service.PostMessageService;
import com.crescendo.score.exception.NoArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class PostMessageController {
    private final PostMessageService postMessageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@Validated @RequestBody SendMessageRequestDTO dto, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean b = postMessageService.sendPostMessage(dto);
            return ResponseEntity.ok().body(b);
        }catch (NoArgumentException | NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
