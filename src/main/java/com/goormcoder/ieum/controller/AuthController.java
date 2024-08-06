package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.request.LoginDto;
import com.goormcoder.ieum.dto.request.MemberCreateDto;
import com.goormcoder.ieum.dto.request.RefreshDto;
import com.goormcoder.ieum.dto.response.JwtTokenDto;
import com.goormcoder.ieum.dto.response.MemberFindDto;
import com.goormcoder.ieum.dto.response.OAuthUserInfoDto;
import com.goormcoder.ieum.jwt.JwtProvider;
import com.goormcoder.ieum.service.KakaoOauthService;
import com.goormcoder.ieum.service.MemberService;
import com.goormcoder.ieum.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth", description = "인증 관련 API")
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final KakaoOauthService kakaoOauthService;

    @Operation(summary = "자체 회원 가입")
    @PostMapping("/join")
    public ResponseEntity<MemberFindDto> create(@RequestBody MemberCreateDto createDto) {
        Member member = memberService.createByLoginId(createDto);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @Operation(summary = "자체 로그인")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginDto loginDto) {
        Member member = memberService.findByLoginIdAndPassword(loginDto.loginId(), loginDto.password());
        JwtTokenDto jwtToken = jwtProvider.generateToken(member);
        refreshTokenService.save(jwtToken.refreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "oAuth 로그인 / 회원가입", description = "registrationId : kakao")
    @GetMapping("/{registrationId}/login")
    public ResponseEntity<JwtTokenDto> oAuth(@PathVariable String registrationId, @RequestParam("code") String code) {
        OAuthUserInfoDto oAuthUserInfoDto = switch (registrationId) {
            case "kakao" -> kakaoOauthService.getUserInfo(code);
            default -> null;
        };
        Member member = memberService.createOrFindByOAuthUserInfo(oAuthUserInfoDto);
        JwtTokenDto jwtToken = jwtProvider.generateToken(member);
        refreshTokenService.save(jwtToken.refreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody RefreshDto refreshDto) {
        String refreshToken = refreshDto.refreshToken();
        if (!refreshTokenService.isExists(refreshToken)) {
            return ResponseEntity.ok("만료되었거나 유효하지 않은 리프래시 토큰입니다.");
        }
        UUID memberId = UUID.fromString(jwtProvider.getMemberId(refreshToken));
        Member member = memberService.findById(memberId);
        String accessToken = jwtProvider.generateAccessToken(member);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 (토큰 만료)")
    public ResponseEntity<String> logout(@RequestBody @RequestParam("token") String token) {
        refreshTokenService.expire(token);
        return ResponseEntity.ok("성공적으로 로그아웃하였습니다.");
    }

}
