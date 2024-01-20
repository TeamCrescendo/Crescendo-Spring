package com.crescendo.inquiry.controller;

import com.crescendo.inquiry.dto.request.InquirySaveRequestDTO;
import com.crescendo.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = {"http://localhost:3000"})
public class InquiryController {
    private final InquiryService inquiryService;

    // 문의 추가하기
    @PostMapping
    public ResponseEntity<?> save(@RequestBody InquirySaveRequestDTO dto){
        boolean save = inquiryService.save(dto);
        return ResponseEntity.ok().body(save);
    }
}
