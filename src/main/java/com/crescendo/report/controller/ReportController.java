package com.crescendo.report.controller;

import com.crescendo.board.dto.response.BoardListResponseDTO;
import com.crescendo.report.dto.requestDTO.ReportRepuestDTO;
import com.crescendo.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    //report 등록
    @PostMapping()
    public ResponseEntity<?> writereport (@Validated @RequestBody
                                              ReportRepuestDTO dto,
                                                BindingResult result){
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 입니다. : {}",result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }try{
            boolean report= reportService.ReportCreate(dto);
            return ResponseEntity.ok().body(report);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
