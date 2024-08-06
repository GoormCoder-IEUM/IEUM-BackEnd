package com.goormcoder.ieum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.goormcoder.ieum.domain.enumeration.Gender;
import com.goormcoder.ieum.dto.response.OAuthUserInfoDto;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOauthService {

    @Value("${oauth.kakao.key}")
    private String clientId;

    @Value("${oauth.kakao.redirectUri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth.kakao.userinfo-uri}")
    private String userinfoUrl;

    public OAuthUserInfoDto getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        Map attributes = getUserInfoAttributes(accessToken);
        Map kakaoAccount = (Map) attributes.get("kakao_account");
        Map profile = (Map) kakaoAccount.get("profile");
        return OAuthUserInfoDto.builder()
                .registrationId("kakao")
                .id(attributes.get("id").toString())
                .name(profile.get("nickname").toString())
                .email(kakaoAccount.get("email").toString())
                .gender(
                        kakaoAccount.get("gender").equals("male")
                                ? Gender.MALE
                                : Gender.FEMALE
                )
                .birth(
                        LocalDate.of(
                                Integer.parseInt(kakaoAccount.get("birthyear").toString()),
                                Integer.parseInt(kakaoAccount.get("birthday").toString().substring(0, 2)),
                                Integer.parseInt(kakaoAccount.get("birthday").toString().substring(2, 4))
                        )
                )
                .build();
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("grant_type", Collections.singletonList("authorization_code"));
        params.put("client_id", Collections.singletonList(clientId));
        params.put("redirect_uri", Collections.singletonList(redirectUri));
        params.put("code", Collections.singletonList(code));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();

        return accessTokenNode.get("access_token").asText();
    }

    private Map getUserInfoAttributes(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(userinfoUrl, HttpMethod.GET, entity, Map.class).getBody();
    }

    public String retrieveAccessToken(String code) {
        return getAccessToken(code);
    }

}
