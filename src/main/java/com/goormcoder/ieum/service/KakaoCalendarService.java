package com.goormcoder.ieum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoCalendarService {

//    @Value("${kakao.api.key}")
//    private String kakaoApiKey;
//
//    @Value("${kakao.oauth.token}")
//    private String kakaoOAuthToken;
//
//    public void createKakaoSubCalendar(String destinationName, LocalDateTime startedAt, LocalDateTime endedAt, String vehicle) {
//        createSubCalendar(destinationName);
//        addEventToCalendar(destinationName, startedAt, endedAt, vehicle);
//    }
//
//    private void createSubCalendar(String destinationName) {
//        String apiUrl = "https://kapi.kakao.com/v1/api/talk/calendar/create/sub";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(kakaoOAuthToken);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("name", "Plan Calendar - " + destinationName);
//        body.put("description", "Plan for " + destinationName);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.postForObject(apiUrl, request, String.class);
//    }
//
//    private void addEventToCalendar(String destinationName, LocalDateTime startedAt, LocalDateTime endedAt, String vehicle) {
//        String apiUrl = "https://kapi.kakao.com/v1/api/talk/calendar/event/add";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(kakaoOAuthToken);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("summary", "Trip to " + destinationName);
//        body.put("description", "Trip details: " + vehicle);
//        body.put("start_at", startedAt.format(DateTimeFormatter.ISO_DATE_TIME));
//        body.put("end_at", endedAt.format(DateTimeFormatter.ISO_DATE_TIME));
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.postForObject(apiUrl, request, String.class);
//    }
}
