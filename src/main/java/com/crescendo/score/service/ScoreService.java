package com.crescendo.score.service;

import com.crescendo.board.entity.Board;
import com.crescendo.member.entity.Member;
import com.crescendo.member.exception.NoMatchAccountException;
import com.crescendo.member.repository.MemberRepository;
import com.crescendo.score.dto.response.FindByAccountScoreResponseDTO;
import com.crescendo.score.entity.Score;
import com.crescendo.score.dto.request.CreateScoreRequestDTO;
import com.crescendo.score.exception.InvalidGenreException;
import com.crescendo.score.exception.NoArgumentException;
import com.crescendo.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final MemberRepository memberRepository;

    // 악보 추가
    public boolean createScore(CreateScoreRequestDTO dto) {
        if (dto == null) {
            throw new NoArgumentException("제대로 된 값 줘");
        }
        boolean flag = memberRepository.existsByAccount(dto.getAccount());
        if (!flag) {
            throw new NoMatchAccountException("계정명이 존재 하지 않습니다");
        }

        String scoreGenre = dto.getScoreGenre();
        boolean invalidGENRE = true;
        Score.GENRE genre = null;
        for (Score.GENRE value : Score.GENRE.values()) {
            // Score.GENRE가 getStringValue() 메서드를 가지고 있는지 확인
            if (value.getStringValue() != null && value.getStringValue().equals(scoreGenre)) {
                invalidGENRE = false;
                genre = value;
                break;  // 일치하는 값 찾으면 반복문 종료
            }
        }

        if (invalidGENRE) {
            throw new InvalidGenreException("유효하지 않는 장르입니다.");
        }

        Member member = memberRepository.getOne(dto.getAccount());
        Score save = scoreRepository.save(Score.builder()
                .scoreTitle(dto.getScoreTitle())
                .scoreGenre(genre)
                .scoreImageUrl(dto.getScoreImageUrl())
                .member(member)
                .build());

        System.out.println("save = " + save);

        return true;
    }

    public List<FindByAccountScoreResponseDTO> findAllByAccount(String account){
        boolean flag = memberRepository.existsByAccount(account);
        if (!flag) {
            throw new NoMatchAccountException("계정명이 존재 하지 않습니다");
        }
        Member member = memberRepository.getOne(account);
        List<Score> allByMember = scoreRepository.findAllByMember(member);

        List<FindByAccountScoreResponseDTO> responseDTOList = new ArrayList<>();
        allByMember.forEach(score -> {
            FindByAccountScoreResponseDTO build = FindByAccountScoreResponseDTO.builder()
                    .account(score.getMember().getAccount())
                    .scoreId(score.getScoreNo())
                    .scoreGenre(score.getScoreGenre().toString())
                    .scoreImageUrl(score.getScoreImageUrl())
                    .scoreTitle(score.getScoreTitle())
                    .build();
            responseDTOList.add(build);
        });
        return responseDTOList;
    }

    public boolean delete(int scoreId){
        boolean flag = scoreRepository.existsById(scoreId);
        if(!flag){
            throw new NoArgumentException("해당 악보는 없습니다");
        }
        scoreRepository.deleteById(scoreId);
        return true;
    }

    // 유튜브 링크 받아서 파이썬 으로 보내기
    public Object postToPython(String url){
        System.out.println("url = " + url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        /*
        youtube 링그 포장된 json
        이런 형식으로
        {
            "url":"youtube url"
        }
         */

        String jsonData = "{\"url\": \"" + url + "\"}";

        log.info("jsonData: " + jsonData);

        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonData, headers);

        String pythonUrl = "http://127.0.0.1:8181/youtube/youtube/";

        //pdf 파일을 받아서 반환해야함.
       Object score_pdf=restTemplate.postForObject(pythonUrl, stringHttpEntity, String.class);
       return  score_pdf;



    }


}
