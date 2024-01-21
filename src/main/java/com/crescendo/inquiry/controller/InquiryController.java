package com.crescendo.inquiry.controller;

import com.crescendo.inquiry.dto.request.InquirySaveRequestDTO;
import com.crescendo.inquiry.dto.response.FoundInquiryListResponseDTO;
import com.crescendo.inquiry.entity.Inquiry;
import com.crescendo.inquiry.service.InquiryService;
import com.crescendo.member.exception.NoMatchAccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = {"http://localhost:3000"})
public class InquiryController {
    private final InquiryService inquiryService;

    // 문의 추가하기
    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody InquirySaveRequestDTO dto, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try {
            List<FoundInquiryListResponseDTO> save = inquiryService.save(dto);
            return ResponseEntity.ok().body(save);
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 계정명으로 문의 전체조회하기
    @GetMapping
    private ResponseEntity<?> findAllByAccount(String account){
        if(account ==null || account.isEmpty()){
            return ResponseEntity.badRequest().body("공백입니다 정확한 계정명을 주세요!");
        }
        log.info("/api/inquiry/GET !! {}", account);
        try{
            List<FoundInquiryListResponseDTO> allByAccount = inquiryService.findAllByAccount(account);
            return ResponseEntity.ok().body(allByAccount);
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 문의 아이디로 문의 삭제하기
    @DeleteMapping
    private ResponseEntity<?> deleteById(String inquiryId){
        if (inquiryId == null || inquiryId.isEmpty()){
            return ResponseEntity.badRequest().body("공백입니다 정확한 아이디를 주세요!");
        }
        log.info("/api/inquiry/Delete !! {}", inquiryId);
        try{
            boolean b = inquiryService.deleteById(inquiryId);
            return ResponseEntity.ok().body(b);
        }catch (NoMatchAccountException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}